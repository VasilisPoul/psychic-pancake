(ns psychic-pancake.orm.user
  (:require
   [psychic-pancake.orm.core :as orm]
   [psychic-pancake.orm.country :as country])
  (:import psychic_pancake.User
           psychic_pancake.User$Role
           psychic_pancake.Message))

(defn map->user [map]
  (orm/hash-map->obj map User))

(defn create! [map]
  (orm/with-session
    (orm/with-transaction
      (orm/persist!
       (map->user
        (assoc map
               :country
               (orm/obj->map (orm/find! psychic_pancake.Country (:country map)))))))))

(defn get-by-id [uid]
  (orm/with-session
    (orm/with-transaction
      (orm/find! User uid))))

(defn remove! [uid]
  (orm/with-session
    (orm/with-transaction
      (orm/remove! (orm/find! User uid)))))

(defn send-message! [msg]
  (orm/with-session
    (orm/with-transaction
      (let [sender (orm/find! User (:from msg))
            receiver (orm/find! User (:to msg))
            message  (orm/hash-map->obj
                      (assoc msg
                             :from sender
                             :to receiver)
                      Message)]
        (orm/merge! message)))))

;; (create!
;;  {:role (name :admin),
;;   :email "foo@test.com",
;;   :first_name "foo",
;;   :last_name "bar",
;;   :uid "test2",
;;   :pending false,
;;   :SSN "2903429570",
;;   :phone_num "123456",
;;   :password_digest "41903wjifdsk",
;;   :location nil,
;;   :inbox [],
;;   :outbox [],
;;   :country {:name "foo"}})

;; (create!
;;  {:role (name :buyer),
;;   :email "foo@test.com",
;;   :first_name "foo",
;;   :last_name "bar",
;;   :uid "test",
;;   :pending false,
;;   :SSN "2903429570",
;;   :phone_num "123456",
;;   :password_digest "41903wjifdsk",
;;   :location nil,
;;   :inbox [],
;;   :outbox [],
;;   :country {:name "foo"}})


;; (get-by-id "test2")

;; (remove! "b")


;; (send-message! {:from "test"
;;                 :to "test2"
;;                 :subject "testasfdfasf"
;;                 :body "test"})


