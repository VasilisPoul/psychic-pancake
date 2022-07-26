(ns psychic-pancake.orm.core
  (:require
   [clojure.walk :refer [keywordize-keys stringify-keys]])
  (:import psychic_pancake.User
           psychic_pancake.User$Role
           org.hibernate.SessionFactory
           org.hibernate.cfg.Configuration
           com.fasterxml.jackson.databind.ObjectMapper               
           com.fasterxml.jackson.annotation.PropertyAccessor         
           com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility))

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
    (keywordize-keys (into {} (convertValue obj java.util.HashMap)))
    {:class (class obj)}))

(defn hash-map->obj
  ([map cls] (convertValue (stringify-keys map) cls))
  ([map] (hash-map->obj map (-> map meta :class))))

