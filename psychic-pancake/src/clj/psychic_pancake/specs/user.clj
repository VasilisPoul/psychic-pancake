(ns psychic-pancake.specs.user
  (:require [psychic-pancake.specs.common :as common]
            [clojure.spec.alpha :as s]
            [spec-tools.core :as st]
            [spec-tools.data-spec :as ds]
            [spec-tools.swagger.core :refer [accept-spec]]))

(s/def :usr/uid
  (st/spec {:spec string?
            :description "User ID"
            :json-schema/default "user_name"
            :swagger/example "user_name"}))

(let [email-pattern "^(.+)@(.+)$"]
  (s/def :usr/email
    (st/spec 
     {:spec (partial re-matches (re-pattern email-pattern))
      :description "Email Address"
      :json-schema/default "test@domain.com"
      :swagger/example "test@domain.com"
      :swagger/type "string"
      :swagger/format "email"
      :swagger/pattern email-pattern})))


(s/def :usr/role
  (st/spec {:spec #{"admin" "seller" "buyer"}
            :description "User Role: One of 'admin', 'seller', 'buyer'."}))


(s/def :usr/rating
  (st/spec {:spec int?
            :description "User Rating"
            :json-schema/default 125
            :swagger/example 125}))

(s/def :usr/location
  (st/spec {:spec string?
            :description "User Location"
            :json-schema/default "Bob's House"
            :swagger/example "Bob's House"}))

(s/def :usr/country
  (st/spec {:spec :common/country-name
            :description "User Country"
            :json-schema/default "Germany"
            :swagger/example "Germany"}))

(s/def :usr/ssn
  (st/spec {:spec string?
            :description "User SSN"
            :json-schema/default "your-ssn-here"
            :swagger/example "ΑΦΜ4567890"}))

(s/def :usr/phone_num
  (st/spec {:spec string?
            :description "User phone number"
            :json-schema/default "yourphonenumber"
            :swagger/example "+306958758231"}))

(s/def :usr/password
  (st/spec {:spec string?
            :description "User password"
            :json-schema/default "your password"
            :swagger/example "password123"}))

(def user-info-shape
  {:username string? :email :usr/email :user-type :usr/role})

(s/def :usr/info-spec
  (st/spec
   {:spec {:username string? :email :usr/email :user-type :usr/role}}))

