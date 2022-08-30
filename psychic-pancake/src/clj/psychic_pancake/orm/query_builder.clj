(ns psychic-pancake.orm.query-builder
  (:require
   [psychic-pancake.orm.core :as orm :refer
    [*session* with-session with-transaction session-factory
     wrap-session]]
   [clojure.set :refer [rename-keys]]
   [clojure.string :refer [join]])
  (:import
   [jakarta.persistence Parameter]
   [org.hibernate Session]
   [org.hibernate.query Query]
   [java.lang IllegalStateException]
   [java.util Map List]))


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

(defn- bind! [^Query q params]
  (let [parameters (group-by
                    (comp
                     {true :named
                      false :positional}
                     nil?
                     (memfn ^Parameter getPosition))
                    (.getParameters q))
        pos-cnt (->> parameters
                     :positional
                     count)
        positional (take pos-cnt params)
        named (apply hash-map (drop pos-cnt params))]
    (do
      (doseq [[p v] (map vector
                         (range 1 (inc (count positional)))
                         positional)]
        (.setParameter q p v))
      
      (doseq [[k v] named]
        (.setParameter q (name k) v))
      q)))

(defn- execute! [^Query q]
  (if (bound? #'*session*)
    (try
      (.getResultList q)
      (catch IllegalStateException e
        (with-transaction (.executeUpdate q))))
    (with-session (execute! q))))

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
         execute!)
     (with-session
       (str->query query-string params))))
  ([^String query-string]
   (partial str->query query-string)))

(defn strs->dbfn [& strs]
  (wrap-session
   #(-> *session*
         (.createQuery (join " " strs))
         (bind %&)
         execute!)))



