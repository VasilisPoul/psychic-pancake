(ns psychic-pancake.orm.country
  (:require
   [psychic-pancake.orm.core :as orm])
  (:import psychic_pancake.Country))

(defn create! [map]
  (orm/save!
   (orm/hash-map->obj map Country)))

(defn get-by-name [name]
  (orm/with-session
    (orm/with-transaction
      (orm/find! Country name))))

;; (create! {:name "Germany" :latitude 1.2 :longitude 1.3})
;; (get-by-name "Germany")
