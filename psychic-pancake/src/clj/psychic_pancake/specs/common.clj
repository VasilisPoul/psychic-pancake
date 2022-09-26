(ns psychic-pancake.specs.common
  (:require [clojure.spec.alpha :as s]
            [spec-tools.core :as st]
            [clojure.data.csv :refer [read-csv]]
            [clojure.java.io :refer [resource reader]]
            [java-time]
            [clojure.string :as string]
            [psychic-pancake.specs.decoders.image :refer
             [this->ImageRef]])
  (:import [javax.xml.bind DatatypeConverter]
           [javax.imageio ImageIO]
           [java.io ByteArrayInputStream]))

(defn resp-404able [resp-schema]
  (assoc resp-schema
         404 {:body {:message (st/spec {:spec string?
                                        :swagger/pattern "404 - Resource not found"
                                        :swagger/example "404 - Resource not found"})}}))

(defn str->image
  ([s]
   (some-> s
       (string/split #",")
       second
       DatatypeConverter/parseBase64Binary
       (ByteArrayInputStream.)
       ImageIO/read))
  ([_ s] (str->image s)))

(s/def :common/image
  (st/spec {:spec (s/and string? (comp not nil? str->image))
            :reason "'%s' is not a valid b64 representation of an image"}))

(s/def :common/image-ref
  (st/spec {:spec string?
            :decode/response #(this->ImageRef %2)}))

(s/def :common/country-name
  (with-open [r (-> "countries.csv" resource reader)]
    (->> r
         read-csv
         rest
         (map #(nth % 3))
         set)))


(def time-formatter (-> "LLL-dd-yy HH:mm:ss"
                        java-time/formatter 
                        (.withZone  (java-time/zone-id))))

(def format-time (comp (partial java-time/format time-formatter)
                       java-time/local-date-time
                       java-time/instant->sql-timestamp
                       java-time/instant))
(def parse-time (partial java-time/instant time-formatter))


(s/def :common/time
  (st/spec
   {:spec (s/and string? (partial re-matches #"\w\w\w-\d\d-\d\d \d\d:\d\d:\d\d"))
    :description "time"
    :decode/json #(format-time %2)
    :decode/response #(format-time %2)}))

;; (st/coerce
;;  :common/time
;;  1659051982906
;;  st/json-transformer)
