(ns psychic-pancake.orm.query-builder
  (:require
   [psychic-pancake.orm.core :as orm :refer
    [*session* with-session with-transaction session-factory]]
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

(defn execute! [^jakarta.persistence.Query q]
  (try
    (.getResultList q)
    (catch IllegalStateException e
      (with-transaction (.executeUpdate q)))))


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



(defprotocol has_bindable_params
  (parameters [this])
  (positional-parameters [this])
  (named-parameters [this])
  (-bind-positional! [this positional])
  (-bind-named! [this named])
  (-bind
    [this positional named]
    [this positional-or-named]))

(do
  (defrecord query
      [^Query query_internal]
    clojure.lang.IFn
    (invoke [this] (let [q (get this :query_internal)]
                     (try
                       (.getResultList q)
                       (catch IllegalStateException e
                         (with-transaction (.executeUpdate q))))))
    has_bindable_params
    (parameters [this] (-> this :query_internal .getParameters))
    (positional-parameters [this]
      (->> this
           parameters
           (filter (memfn ^Parameter getPosition))))
    (named-parameters [this]
      (->> this
           parameters
           (filter (memfn ^Parameter getName))))
    (-bind-positional! [this positional]
      (do
        (doseq [[p v] (map vector
                           (range 1 (inc (count positional)))
                           positional)]
          (.setParameter (:query_internal this) p v)))
        this)
    (-bind-named! [this named]
      (do
        (doseq [[k v] named]
          (.setParameter (:query_internal this) (name k) v))
        this))
    (-bind [this positional named]
      (-> this
          (.clone)
          (-bind-positional! positional)
          (-bind-named! named)))
    Object
    (clone [this] (let [qstr (-> this
                                 :query_internal
                                 .getQueryString)]
                    (-> session-factory
                        .openSession
                        (.createQuery qstr)
                        query.))))


  (ns-unmap *ns* '->query)

  (defprotocol to_query
    (->query [this] "Convert this to a query"))
  (extend-protocol to_query
    String
    (->query [this] (-> session-factory
                        .openSession
                        (.createQuery this)
                        ->query))
    Query
    (->query [this] (query. this))))

(defn bind [^query this & params]
  (let [pos-cnt (-> this
                    positional-parameters
                    count)
        positional (take pos-cnt params)
        named (apply hash-map (drop pos-cnt params))]
    (-bind this positional named)))

(defn strs->dbfn [& s]
  (comp
   #(%)
   (->> s
        (join " ")
        ->query
        (partial bind))))



(with-session
  (.getSingleResult
   (.createNativeQuery *session* "SELECT u.uid FROM main.user u")))

