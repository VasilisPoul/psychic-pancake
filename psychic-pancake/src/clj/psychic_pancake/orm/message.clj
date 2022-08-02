(ns psychic-pancake.orm.message
  (:require
   [psychic-pancake.orm.core :as orm]
   [psychic-pancake.orm.user :as orm.user])
  (:import psychic_pancake.User
           psychic_pancake.Message))


(defn create! [msg]
  (orm/with-session
    (orm/with-transaction
      (let [message (orm/hash-map->obj (dissoc msg :from :to)
                                       Message)]
        (when (every? (comp not nil? msg) [:from :to])
          (.setFrom message (:from msg))
          (.setTo message (:to msg))
          (orm/merge! message))))))

(defn get-by-id [uid]
  (orm/with-session
    (orm/with-transaction
      (orm/find! Message uid))))

;; (create!
;;  {:from "user_name",
;;   :to "user_name"
;;   :subject "subj"
;;   :body "body"})

;; (get-by-id 1)

;; (orm/obj->map (get-by-id 1))
