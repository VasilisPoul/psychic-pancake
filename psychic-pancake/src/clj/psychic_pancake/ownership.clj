(ns psychic-pancake.ownership
  (:import psychic_pancake.User
           psychic_pancake.Message
           psychic_pancake.Listing
           psychic_pancake.Bid))

(defmulti owner (fn [thing] (class thing)))

(defmethod owner User [^User thing]
  thing)

(defmethod owner Listing [^Listing thing]
  (.getSeller thing))

(defmethod owner Bid [^Bid thing]
  (.getBidder thing))

(defmethod owner :default [thing]
  nil)


(defmulti owns? (fn [user _] (class user)))

(defmethod owns? String [^String uid thing]
  (= uid (.getUid thing)))

(defmethod owns? User [^User user thing]
  (owns? (.getUid user) thing))

