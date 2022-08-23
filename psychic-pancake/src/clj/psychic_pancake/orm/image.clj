(ns psychic-pancake.orm.image
  (:require
   [psychic-pancake.orm.core :as orm])
  (:import psychic_pancake.Image))

(defn create! [map]
  (orm/save!
   (orm/hash-map->obj map Image)))

(defn get-by-id [id]
  (orm/with-session
    (orm/with-transaction
      (orm/find! Image id))))

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


