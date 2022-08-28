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
         "where m.from = :uid "
         "and m.sender_deleted = false"))
   (partial hash-map :uid)))

(def get-inbox
 (comp
  (str->query
   (str "select m from Message m "
        "where m.to = :uid "
        "and m.receiver_deleted = false"))
  (partial hash-map :uid)))


(def delete-from
  (let [args->map #(hash-map :uid %1 :id %2)
        str->query #(-> % str->query (comp args->map))]
    (juxt
     (str->query
      (str "update Message m "
           "set m.receiver_deleted = true "
           "where m.msg_id=:id and m.to.uid=:uid"))
     (str->query
      (str "update Message m "
           "set m.sender_deleted = true "
           "where m.msg_id=:id and m.from.uid=:uid"))
     (str->query
      (str "delete Message m "
           "where m.sender_deleted = true "
           "and m.receiver_deleted = true "
           "and m.msg_id=:id "
           "and ((:uid = m.from.uid) or (:uid = m.to.uid))")))))

;; (create!
;;  {:from "user_name",
;;   :to "user_name"
;;   :subject "subj"
;;   :body "body"})

;; (get-by-id 1)

;; (orm/obj->map (get-by-id 1))
