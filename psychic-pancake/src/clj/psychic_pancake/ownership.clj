(ns psychic-pancake.ownership
  (:import [psychic_pancake
            User Message Listing Bid Notification]))

(defn ^String User->uid [^User u]
  (.getUid u))

(defmulti owner (fn [thing] (class thing)))

(defmethod owner User [^User thing]
  thing)

(defmethod owner Listing [^Listing thing]
  (.getSeller thing))

(defmethod owner Bid [^Bid thing]
  (.getBidder thing))

(defmethod owner Message [^Message thing]
  [(.getFrom thing)
   (.getTo thing)])

(defmethod owner Notification [^Notification thing]
  (.getUser thing))

(defmethod owner :default [thing]
  nil)


(defmulti owns? (fn [user _] (class user)))

(defmethod owns? String [^String uid thing]
  (let [thing-owner (owner thing)]
    (if (coll? thing-owner)
      (contains? (set (map User->uid thing-owner)) uid)
    (= uid (-> thing owner User->uid)))))

(defmethod owns? User [^User user thing]
  (owns? (.getUid user) thing))

(defmethod owns? :default [_ _]
  false)
