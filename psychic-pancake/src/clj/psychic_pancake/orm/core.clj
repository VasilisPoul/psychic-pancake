(ns psychic-pancake.orm.core
  (:require
   [clojure.walk :as w :refer [keywordize-keys stringify-keys]])
  (:import
   [org.hibernate SessionFactory Session]
   org.hibernate.cfg.Configuration
   [com.fasterxml.jackson.databind ObjectMapper DeserializationFeature]
   [com.fasterxml.jackson.annotation PropertyAccessor JsonAutoDetect$Visibility]))

(defprotocol java->clj
  (->clj [this]))

(declare obj->map)

(extend-protocol java->clj
  java.util.Set
  (->clj [this]
    (into #{} this))
  java.util.Map
  (->clj [this] (into
                   {}
                   (map (fn [[k v]] [(keyword k)
                                    (->clj v)]) this)))
  java.util.List
  (->clj [this]
    (map ->clj this))
  Long
  (->clj [this] this)
  String
  (->clj [this] this)
  Double
  (->clj [this] this)
  Boolean
  (->clj [this] this)
  Object
  (->clj [this] (-> this
                    obj->map
                    ->clj))
  nil
  (->clj [this] this))



;; (deftype ^{Entity {:name "entity_test"}
;;            Table {:name "entity_test" :schema "main"}}
;;     entity-test
;;     [^{:tag int Id {}} id])

(def session-factory
    (-> (Configuration.)
        .configure
        .buildSessionFactory))

(def ^:dynamic *session*)

;; inspiration from clj-jpa
(defmacro with-session [& body]
  `(binding [*session* (.openSession session-factory)]
     (do ~@body)))

(defmacro with-transaction [& body]
  `(let [trx# (.getTransaction *session*)]
     (.begin trx#)
     (let [ret# (do ~@body)]
       (.commit trx#)
       ret#)))

(defn wrap-session [f]
  #(if (bound? #'*session*)
     (apply f %&)
     (with-session (apply f %&))))

(defn wrap-transaction [f]
  (wrap-session
   #(with-transaction (apply f %&))))

(fn [^Session a & rest]
  (.find a ))

(memfn .find a b)

(with-session (partial  *session*))

(defmacro def-orm-fn [fname method params]
  `(defn 
     ~fname
     ~params
     (let [func# (memfn ^Session ~method ~@params)]
       (if (bound? #'*session*)
         (func# *session* ~@params)
         (with-session
           (with-transaction
             (func# *session* ~@params)))))))




(def-orm-fn merge! merge [ent])
(def-orm-fn persist! persist [ent])
(def-orm-fn remove! remove [ent])
(def-orm-fn find! find [cls id])

(defn refresh! [ent]
  (if (bound? #'*session*)
    (do
      (.refresh *session* ent)
      ent)
    (with-session
      (with-transaction
        (refresh! ent)))))

(def save! merge!)

(def DefaultObjectMapper
  (doto (ObjectMapper.)
    (.setVisibility PropertyAccessor/FIELD JsonAutoDetect$Visibility/ANY)
    (.configure DeserializationFeature/FAIL_ON_UNKNOWN_PROPERTIES false)))

(defn- convertValue [obj cls]
  (.convertValue DefaultObjectMapper obj cls))


(defn obj->map [obj]
  (with-meta
    (->clj (into {} (convertValue obj java.util.HashMap)))
    {:class (class obj)}))

(defn hash-map->obj
  ([map cls] (convertValue (stringify-keys map) cls))
  ([map] (hash-map->obj map (-> map meta :class))))
