(ns psychic-pancake.orm.bid
  (:require [psychic-pancake.orm.core :as orm]
            [psychic-pancake.specs.common :refer [parse-time]])
  (:import (psychic_pancake Bid Listing)))


(orm/hash-map->obj (orm/obj->map (orm/find! Listing 1)))


(orm/obj->map
 (doto (orm/hash-map->obj {:id 2} Bid)
   (.setListing (orm/find! Listing 1))))

(defn create! [params]
  (-> params
      (dissoc :listing)
      (orm/hash-map->obj Bid)
      (doto (.setListing (:listing params)))
      (orm/merge!)))

(defn get-by-id [id]
  (orm/with-session
    (orm/with-transaction
      (orm/find! Bid id))))


;; (create!
;;  {:amount 12.37
;;   :listing (psychic-pancake.orm.listing/get-by-id 6)
;;   :bidder (psychic-pancake.orm.user/get-by-id "user_name")})
