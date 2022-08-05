(ns psychic-pancake.orm.core
  (:require
   [clojure.walk :as w :refer [keywordize-keys stringify-keys]])
  (:import
   org.hibernate.SessionFactory
   org.hibernate.cfg.Configuration
   com.fasterxml.jackson.databind.ObjectMapper               
   com.fasterxml.jackson.annotation.PropertyAccessor         
   com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility))

(defprotocol java->clj
  (->clj [this]))

(extend-protocol java->clj
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
       (let [ret# (do ~@body)]
         (.begin trx#)
         (.commit trx#)
         ret#)))

(defn merge! [ent]
  (.merge *session* ent))

(defn remove! [ent]
  (.remove *session* ent))

(defn save! [ent]
  (with-session
    (with-transaction
      (merge! ent))))

(defn find! [cls id]
  (.find *session* cls id))

(defn refresh! [ent]
  (.refresh *session* ent))

(def DefaultObjectMapper
  (let [om (ObjectMapper.)]
    (.setVisibility om PropertyAccessor/FIELD JsonAutoDetect$Visibility/ANY)
    om))

(defn- convertValue [obj cls]
  (.convertValue DefaultObjectMapper obj cls))


(defn obj->map [obj]
  (with-meta
    (->clj (into {} (convertValue obj java.util.HashMap)))
    {:class (class obj)}))

(defn hash-map->obj
  ([map cls] (convertValue (stringify-keys map) cls))
  ([map] (hash-map->obj map (-> map meta :class))))

(import '[psychic_pancake User Listing])



