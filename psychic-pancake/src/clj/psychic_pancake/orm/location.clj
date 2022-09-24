(ns psychic-pancake.orm.location
  (:require
   [psychic-pancake.orm.core :as orm])
  (:import psychic_pancake.Location))

(defn create! [map]
  (orm/save!
   (Location.
    (:name map)
    (:latitude map)
    (:longitude map))))

(defn get-by-name [name]
  (orm/with-session
    (orm/with-transaction
      (orm/find! Location name))))

(defn remove! [name]
  (orm/with-session
    (orm/with-transaction
      (orm/remove! (orm/find! Location name)))))

;; (create! {:name "string" :latitude 1.2 :longitude 1.3})
;; (get-by-name "foo")
