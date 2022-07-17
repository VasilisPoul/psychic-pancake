(ns psychic-pancake.orm)

(import psychic_pancake.User psychic_pancake.User$Role)

(import org.hibernate.SessionFactory)
(import org.hibernate.cfg.Configuration)

(require '[clojure.walk :refer [keywordize-keys stringify-keys]])

(deftype ^{Entity {:name "entity_test"}
           Table {:name "entity_test" :schema "main"}}
    entity-test
    [^{:tag int Id {}} id])

(def session-factory
    (-> (Configuration.)
        .configure
        .buildSessionFactory))

(def ^:dynamic *session*)

;; inspiration from clj-jpa
(defmacro with-session [& body]
  `(binding [*session* (.openSession session-factory)]
     ~@body))

(defmacro with-transaction [& body]
  `(let [trx# (.getTransaction *session*)]
       (let [ret# (do ~@body)]
         (.begin trx#)
         (.commit trx#)
         (obj->map ret#))))


(import com.fasterxml.jackson.databind.ObjectMapper
        com.fasterxml.jackson.annotation.PropertyAccessor
        com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility)

(defn obj->map [obj]
  (let [om (ObjectMapper.)]
    (.setVisibility om PropertyAccessor/FIELD JsonAutoDetect$Visibility/ANY)
    (keywordize-keys (into {} (.convertValue om obj java.util.HashMap)))))

(defn map->obj [t m]
  (.convertValue DefaultObjectMapper (stringify-keys m) t))


(import jakarta.persistence.Entity
        jakarta.persistence.Table
        jakarta.persistence.Id)



(seq (.getFields entity-test))

(seq (.getAnnotations entity-test))
(seq (.getAnnotations (.getField entity-test "id")))

 (with-session
  (with-transaction
    (.merge *session* (User. "foo" "foo@domain.org" User$Role/admin))))

(obj->map (hash-map->obj {:uid "foo", :role "admin", :email "foo@domain.org"} User))

(def DefaultObjectMapper
  (let [om (ObjectMapper.)]
    (.setVisibility om PropertyAccessor/FIELD JsonAutoDetect$Visibility/ANY)
    om))



((partial #(.convertValue DefaultObjectMapper %1 %2)
   (stringify-keys {:uid "foo" :email "test" :role "admin"})) User)

(def hash-map->obj #(.convertValue DefaultObjectMapper (stringify-keys %1) %2))

(obj->map (hash-map->obj {:uid "foo" :email "test" :role "admin"} User))

(hash-map->obj {:uid "foo" :email "test" :role "admin"})

(let [session (.openSession session-factory)]
  (let [transaction (.getTransaction session)]
    (do (.persist session (User. "foo" "foo@domain.com" User$Role/admin))
        (.begin transaction)
        (.commit transaction))))

(meta {:foo 1})
