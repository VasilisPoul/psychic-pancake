(ns psychic-pancake.specs.responses
  (:require
   [clojure.spec.alpha :as s]
   [spec-tools.core :as st]
   [spec-tools.data-spec :as ds]))

(def status-401-shape
  {:body {:reason (st/spec
                   {:spec string?
                    :swagger/example "Invalid Token"})
          :info (st/spec
                 {:spec string?
                  :swagger/example
                  (str
                   "Get a valid auth token using the "
                   "/api/token endpoint and use it "
                   "in the 'Authorization' "
                   "header.")})}})

(def status-404-shape
  {:body {:reason (st/spec
                   {:spec string?
                    :swagger/example "Not Found"})
          :info (s/coll-of string?)}})
