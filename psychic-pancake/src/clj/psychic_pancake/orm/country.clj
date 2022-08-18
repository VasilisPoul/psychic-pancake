(ns psychic-pancake.orm.country
  (:require
   [psychic-pancake.orm.core :as orm]
   [clojure.java.io :refer [resource reader]]
   [clojure.data.csv :refer [read-csv]]
   [clojure.set :refer [rename-keys]])
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


;; create countries in db
;; (with-open [reader (reader (resource "countries.csv"))]
;;   (let [keys (map keyword (-> reader read-csv first))]
;;     (doseq [country (->> reader
;;                          read-csv
;;                          (drop 1)
;;                          (map (comp #(rename-keys % {:country :name})
;;                                     #(update % :longitude parse-double)
;;                                     #(update % :latitude parse-double)
;;                                     #(select-keys % [:country :latitude :longitude])
;;                                     (partial into {})
;;                                     (partial map vector keys))))]
;;       (create! country))))


