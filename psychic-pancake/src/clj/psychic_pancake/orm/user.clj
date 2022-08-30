(ns psychic-pancake.orm.user
  (:require
   [psychic-pancake.orm.core :as orm]
   [psychic-pancake.orm.country :as country]
   [psychic-pancake.orm.query-builder :refer [strs->dbfn]]
   [clojure.set :refer [union]])
  (:import [psychic_pancake User User$Role Message Country]))

(defn map->user [map]
  (orm/hash-map->obj map User))

(defn create! [map]
  (do
    (orm/with-session
      (orm/with-transaction
        (let [country (orm/find! Country (:country map))
            user (map->user
                  (assoc map
                         :country country))]
          (orm/persist! user)
          user)))))

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

(def uid->uids-i-sold-to
  (comp
   set
   (strs->dbfn
    "select b.bidder.uid from User u"
    "left join Bid b on b.bidder = u"
    "left join Listing l on b.listing = l"
    "where l.seller.uid=?1"
    "and b.amount ="
    "(select max(b_.amount) from Bid b_"
    "where b_.listing = l)")))

(def uid->uids-that-bought-from-me
  (comp
   set
   (strs->dbfn
    "select l.seller.uid from User u"
    "left join Bid b on b.bidder = u"
    "left join Listing l on b.listing = l"
    "where b.bidder.uid=?1"
    "and b.amount ="
    "(select max(b_.amount) from Bid b_"
    "where b_.listing = l)")))

(def uid->uids-that-i-can-message
  (comp
   (partial apply union)
   (juxt uid->uids-i-sold-to uid->uids-that-bought-from-me)))

(def can-message?
  (comp
   first
   (strs->dbfn
    "select count(l.seller.uid) > 0 from User u"
    "left join Bid b on b.bidder = u"
    "left join Listing l on b.listing = l"
    "where ((b.bidder.uid=?1"
    "and l.seller.uid=?2)"
    "or (b.bidder.uid=?2"
    "and l.seller.uid=?1))"
    "and b.amount ="
    "(select max(b_.amount) from Bid b_"
    "where b_.listing = l)")))

(def get-all-users
  (strs->dbfn
   "select u from User u"))

(def get-pending-users
  (strs->dbfn
   "select u from User u"
   "where u.pending = true"))

(def pending-uid->respond!
  (strs->dbfn
   "update User u"
   "set u.pending = :accept"
   "where u.uid=:uid"))



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
;;   :country "Germany"})


;; (get-by-id "test2")

;; (remove! "b")


;; (send-message! {:from "test"
;;                 :to "test2"
;;                 :subject "testasfdfasf"
;;                 :body "test"})


