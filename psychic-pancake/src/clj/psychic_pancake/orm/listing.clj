(ns psychic-pancake.orm.listing
  (:require [psychic-pancake.orm.core :as orm]
            [psychic-pancake.specs.common :refer [parse-time]]
            [psychic-pancake.orm.core :as orm]
            [psychic-pancake.orm.query-builder :refer
             [str->query strs->dbfn]]
            [psychic-pancake.orm.notifications :refer
             [notifications-of ends-notification]]
            [java-time :as jt])
  (:import (psychic_pancake User Listing Category Image)))

(defn create! [params]
  (-> params
      (update :categories
              (partial map #(orm/merge! (Category. %))))
      (update :images
              (partial map
                       #(->> %
                             (Image. nil (:seller params))
                             orm/merge!)))
      (assoc :started (jt/instant->sql-timestamp (jt/instant)))
      (update :ends parse-time)
      (assoc :bids [])
      (assoc :activated false)
      (orm/hash-map->obj Listing)
      orm/merge!
      orm/refresh!))

(defn update! [current params]
  (let [cmap (orm/->clj current)
        images (.getImages current)
        new-ends (some-> params :ends parse-time)]
    (do
      (when (not (nil? new-ends))
        (orm/merge!
         (doto (ends-notification current)
           (.setDisplayAt new-ends))))
      (-> cmap
          (merge params)
          (#(if new-ends
              (assoc % :ends new-ends) %))
          (assoc :images images)
          (orm/hash-map->obj Listing)
          orm/merge!
          orm/refresh!))))

;; (defn activate! [listing]
  
;;   (orm/with-session
;;     (orm/with-transaction
;;       (-> (doto listing
;;             (.setActivated true)
;;             (.setStarted (jt/instant->sql-timestamp (jt/instant))))
;;           orm/merge!))))

(defn activate! [listing]
  (orm/with-session
    ((strs->dbfn "UPDATE Listing as l"
                 "SET"
                 "l.activated = TRUE"
                 "WHERE l.item_id=?1") (.getItem_id listing))
    (orm/refresh! listing)))

(defn get-by-id [^Long id]
   (orm/with-session
    (orm/with-transaction
      (orm/find! Listing id))))

(defn delete-by-id [^Long id]
  (orm/with-session
    (orm/with-transaction
      (let [l (orm/find! Listing id)
            notifs (notifications-of l)]
        (doseq [n (apply vector notifs)]
          (orm/remove! n))
        (orm/remove! l)))))





(def search-listings
  (comp
   (->
    (str "select l from Listing l "
         "left join l.categories c "
         "where (l.name like :name or :name = NULL) "
         "and (%1$s >= :price_min or :price_min = NULL) "
         "and (%1$s <= :price_max or :price_max = NULL) "
         "and (l.country.name = :country or :country = NULL) "
         "and (:seller_rating = NULL or l.seller.rating >= :seller_rating) "
         "and (NOW() < l.ends or :only_active = FALSE) "
         "and (c.name in :categories or coalesce(:categories) IS NULL) "
         "and (l.seller.rating >= :seller_rating or :seller_rating = NULL) "
         "and (l.seller.uid = :seller_uid or :seller_uid = NULL) "
         #_("and (:radius = NULL or (POWER(:position_lon - l.location.longitude, 2) "
            " + POWER(:position_lat - l.location.latitude, 2)) * 111 <= :radius)")
         "and (l.ends > :after) "
         "ORDER BY l.ends")
    (format (str "(case (select count(b) from Bid b where b.listing = l) "
                 " when 0 then l.first_bid "
                 "else (select max(b.amount) from Bid b where b.listing = l) end)"))
    str->query)
   (partial merge {:name nil
                   :country nil
                   :price_min nil
                   :price_max nil
                   :seller_rating nil
                   :categories nil
                   :seller_uid nil
                   :only_active true
                   :after (jt/instant->sql-timestamp (jt/instant))
                   ;; :radius nil
                   ;; :position_lon nil
                   ;; :position_lat nil
                   })))

(def get-by-winner
  (strs->dbfn
   "SELECT lst FROM Listing lst"
   "RIGHT JOIN Bid b ON"
   "amount = "
   "(SELECT MAX(b.amount) FROM Bid b WHERE b.listing = lst)"
   "WHERE b.bidder.uid=?1"
   "AND (NOW() > lst.ends or :show_active = TRUE)"))
