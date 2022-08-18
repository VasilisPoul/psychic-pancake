(ns psychic-pancake.orm.query-builder
  (:require
   [psychic-pancake.orm.core :as orm :refer [*session* with-session]])
  (:import
   [org.hibernate Session]
   [javax.persistence.criteria CriteriaQuery CriteriaBuilder]))

;; (def ^:dynamic *builder*)
;; (def ^:dynamic *query*)
;; (def ^:dynamic *root*)

;; (defmacro with-context [cls root & body]
;;   `(with-session
;;      (binding [*builder* (.getCriteriaBuilder *session*)]
;;        (binding [*query* (.createQuery *builder* ~cls)]
;;          (binding [*root* (.from *query* ~root)]
;;           ~@body
;;           (.getResultList *query*))))))


;; (defn select [what]
;;   (.select *query* what))

;; (defn where [what]
;;   (.where *root* what))

;; (defn $= [a b]
;;   (.equal *builder* a b))

;; (defn get [rt ^String field]
;;   (.get *root* field))

;; (import [psychic_pancake User])

(defn query->param-names [q]
  (->> q
      .getParameters
      (map #(.getName %))
      set))

(defn query->unbound-params [q]
  (let [names (query->param-names q)
        binds (.getQueryParameterBindings q)]
    (->> names
         (filter #(not (.isBound (.getBinding binds %))))
         set)))

(defn query-bind-params [q m]
  (doseq [[k v] m]
    (.setParameter q (name k) v))
  q)

(defn str->query
  ([^String query-string params]
   (if (bound? #'*session*)
     (-> *session*
         (.createQuery query-string)
         (query-bind-params params)
         .getResultList)
     (with-session (str->query query-string params))))
  ([^String query-string]
   (partial str->query query-string)))
 
;; (->
;;  (with-session
;;    (str->query
;;                (str "select b.bidder.uid from User u "
;;                     "left join Bid b on b.bidder = u "
;;                     "left join Listing l on b.listing = l "
;;                     "where l.seller.uid=:uid "
;;                     "and b.amount = (select max(b_.amount) from Bid b_ where b_.listing = l)")))
 
;;  query->unbound-params)

(str->query
          (str "select b.bidder.uid from User u "
               "left join Bid b on b.bidder = u "
               "left join Listing l on b.listing = l "
               "where l.seller.uid=:uid "
               "and b.amount = (select max(b_.amount) from Bid b_ where b_.listing = l)")
          {:uid "user_name"})


(let [f (str->query
          (str "select b.bidder.uid from User u "
               "left join Bid b on b.bidder = u "
               "left join Listing l on b.listing = l "
               "where l.seller.uid=:uid "
               "and b.amount = (select max(b_.amount) from Bid b_ where b_.listing = l)"))]
  (f {:uid "user_name"}))

;; (bound? #'*session*)
