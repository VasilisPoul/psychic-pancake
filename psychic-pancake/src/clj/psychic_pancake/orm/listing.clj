(ns psychic-pancake.orm.listing
  (:require [psychic-pancake.orm.core :as orm]
            [psychic-pancake.specs.common :refer [parse-time]])
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
