(ns psychic-pancake.orm.message
  (:require
   [psychic-pancake.orm.core :as orm]
   [psychic-pancake.orm.user :as orm.user]
   [psychic-pancake.orm.query-builder :refer [str->query]])
  (:import [psychic_pancake User Message]
           [org.hibernate.query IllegalSelectQueryException]))


(def Message->from
  (memfn ^Message getFrom))

(def Message->to
  (memfn ^Message getTo))


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

(def get-outbox 
  (comp
   (str->query
    (str "select m from Message m "
         "where m.sender = :uid "
         "and m.sender_deleted = false"))
   (partial hash-map :uid)))

(def get-inbox
 (comp
  (str->query
   (str "select m from Message m "
        "where m.from = :uid "
        "and m.receiver_deleted = false"))
  (partial hash-map :uid)))


(def delete-from
  (juxt
   (comp
    (str->query
     (str "update Message m "
          "set m.receiver_deleted = true "
          "where m.id=:id"))
    (partial hash-map :id))
   (comp
    (str->query
     (str "update Message m "
          "set m.sender_deleted = true "
          "where m.id=:id"))
    (partial hash-map :id))))

;; (create!
;;  {:from "user_name",
;;   :to "user_name"
;;   :subject "subj"
;;   :body "body"})

;; (get-by-id 1)

;; (orm/obj->map (get-by-id 1))
