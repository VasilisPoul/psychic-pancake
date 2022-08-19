(ns psychic-pancake.orm.listing
  (:require [psychic-pancake.orm.core :as orm]
            [psychic-pancake.specs.common :refer [parse-time]]
            [psychic-pancake.orm.query-builder :refer [str->query]])
  (:import (psychic_pancake User Listing Category)))

(defn create! [params]
  (orm/with-session
    (orm/with-transaction
      (orm/merge!
       (orm/hash-map->obj
        (-> params
         (update :categories
                 (fn [cats]
                   (map #(orm/with-transaction
                           (orm/merge!
                            (Category. %)))
                        cats)))
         ;; (update :started parse-time)
         (update :ends parse-time))
        Listing)))))

(defn get-by-id [^Long id]
  (->
   (orm/with-session
    (orm/with-transaction
      (orm/find! Listing id)))))

(defn delete-by-id [^Long id]
  (->
   (orm/with-session
    (orm/with-transaction
      (let [l (orm/find! Listing id)]
        (orm/remove! l))))))

(def search-listings
  (comp
   (->
    (str "select l from Listing l "
         "where (l.name like :name or :name = NULL) "
         "and (%1$s >= :price_min or :price_min = NULL) "
         "and (%1$s <= :price_max or :price_max = NULL) "
         "and (l.country.name = :country or :country = NULL) "
         "and (:seller_rating = NULL or l.seller.rating >= :seller_rating) "
         "and (:today < l.ends or :only_active = FALSE)")
    (format (str "(case (select count(b) from Bid b where b.listing = l) "
                 " when 0 then l.first_bid "
                 "else (select max(b.amount) from Bid b where b.listing = l) end)"))
    str->query)
   (partial merge {:name nil
                   :country nil
                   :price_min nil
                   :price_max nil
                   :seller_rating nil
                   :only_active true
                   :today (java.util.Date.)})))


;; (orm/->clj (search-listings {}))
