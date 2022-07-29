(ns psychic-pancake.orm.message
  (:require
   [psychic-pancake.orm.core :as orm]
   [psychic-pancake.orm.user :as orm.user])
  (:import psychic_pancake.User
           psychic_pancake.Message))


(defn create! [msg]
  (orm/with-session
    (orm/with-transaction
      (let [sender (orm/find! User (:from msg))
            receiver (orm/find! User (:to msg))
            message  (orm/hash-map->obj
                      (assoc msg
                             :from sender
                             :to receiver)
                      Message)]
        (when (every? (complement nil?) [sender receiver])
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
