(ns psychic-pancake.orm.notifications
  (:require [psychic-pancake.orm.core :as orm]
            [psychic-pancake.orm.query-builder :refer
             [strs->dbfn]]
            [java-time :refer [instant instant->sql-timestamp]])
  (:import (psychic_pancake Notification Listing Bid Message)))

(def get-by-id
  (partial orm/find! Notification))

(def check
  (comp
   (strs->dbfn
   "SELECT n.id FROM Notification n"
   "WHERE n.displayAt < NOW()"
   "AND :user = n.user OR n.user = NULL"
   "AND :show_all = TRUE OR n.is_seen = FALSE")))

(def listing->notification
  (strs->dbfn
   "SELECT n FROM Notification n"
   "WHERE n.listing_ref.item_id=?1"))

(def bid->notification
  (strs->dbfn
   "SELECT n FROM Notification n"
   "WHERE n.bid_ref.id=?1"))

(def message->notification
  (strs->dbfn
   "SELECT n FROM Notification n"
   "WHERE n.message_ref.msg_id=?1"))

(defn create! [m]
  (-> m
      (orm/hash-map->obj Notification)
      orm/save!))

(defn- now []
  (instant->sql-timestamp
   (instant)))

(defn notify-listing-ends [^Listing listing]
  (create!
   {:user (.getSeller listing)
    :displayAt (.getEnds listing)
    :listing_ref listing}))

(defn notify-new-bid [^Bid bid]
  (create!
   {:user (-> bid .getListing .getSeller)
    :displayAt (now)
    :bid_ref bid}))

(defn notify-new-message [^Message msg]
  (create!
   {:user (.getTo msg)
    :displayAt (now)
    :message_ref msg}))

(defmulti mark-seen! class)

(defmethod mark-seen! Message [^Message msg]
  ((strs->dbfn
    "UPDATE Notification AS n"
    "SET is_seen = TRUE"
    "WHERE n.message_ref.msg_id=?1"
    "AND n.displayAt < NOW()")
   (.getMsg_id msg)))

(defmethod mark-seen! Bid [^Bid bid]
  ((strs->dbfn
    "UPDATE Notification AS n"
    "SET is_seen = TRUE"
    "WHERE n.bid_ref.id=?1"
    "AND n.displayAt < NOW()")
   (.getId bid)))

(defmethod mark-seen! Listing [^Listing listing]
  ((juxt
    (strs->dbfn
     "UPDATE Notification n"
     "SET is_seen = TRUE"
     "WHERE n.listing_ref.item_id=?1")
    (strs->dbfn
     "UPDATE Notification n"
     "SET is_seen = TRUE"
     "WHERE n.bid_ref.id in"
     "(SELECT b.id FROM Bid b"
     "WHERE b.listing.item_id=?1)"))
   (.getItem_id listing)))



