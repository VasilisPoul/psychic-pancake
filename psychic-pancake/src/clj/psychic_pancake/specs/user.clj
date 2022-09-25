(ns psychic-pancake.specs.user
  (:require [psychic-pancake.specs.common :as common]
            [clojure.spec.alpha :as s]
            [spec-tools.core :as st]
            [spec-tools.data-spec :as ds]
            [spec-tools.swagger.core :refer [accept-spec]]
            [psychic-pancake.orm.core :as orm]
            [psychic-pancake.orm.user :as orm.user]
            [psychic-pancake.specs.decoders.user
             :refer [this->UserRef ->CountryName]]))

(s/def :usr/uid
  (st/spec {:spec string?
            :description "User ID"
            :json-schema/default "user_name"
            :swagger/example "user_name"}))



(s/def :usr/ref
  (st/spec {:spec (s/and string? (partial re-matches #"/api/user/.*"))
            :description "User Reference"
            :json-schema/default "/api/user/user_name"
            :swagger/example "/api/user/<uid>"
            :swagger/pattern "/api/user/.*"
            :reason "invalid user url"
            :decode/response #(this->UserRef %2)}))

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
  (ds/spec
   {:spec {:name string?
           :latitude number?
           :longitude number?}
    :name ::location
    :swagger/example {:name "Bob's House" :longitude 10.2221 :latitude 32.822222222221}}))

(s/def :usr/country
  (st/spec {:spec :common/country-name
            :description "User Country"
            :json-schema/default "Germany"
            :swagger/example "Germany"}))

(s/def :usr/vat
  (st/spec {:spec string?
            :description "User VAT"
            :json-schema/default "your-vat-here"
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

(s/def :usr/first_name
  (st/spec {:spec string?
            :description "User's first name"
            :json-schema/default "Firstname"
            :swagger/example "John"}))

(s/def :usr/last_name
  (st/spec {:spec string?
            :description "User's last name"
            :json-schema/default "Lastname"
            :swagger/example "Wick"}))


(s/def :usr/existing
  (st/spec
   {:spec #(-> % class (= psychic_pancake.User))
    :type psychic_pancake.User
    :decode/json #(orm.user/get-by-id %2)
    :reason "User '%s' does not exist."}))


(def user-info-shape
  "Shape of a user's information"
  {:role :usr/role
   :uid :usr/uid
   (ds/opt :first_name) :usr/first_name
   (ds/opt :last_name) :usr/last_name
   (ds/opt :email) :usr/email
   (ds/opt :VAT) :usr/vat
   (ds/opt :phone_num) :usr/phone_num
   (ds/opt :country) :usr/country
   (ds/opt :location) :usr/location
   (ds/opt :rating) :usr/rating})



(def user-registration-shape
  "Shape used to validate the registration form"
  {:role :usr/role
   :uid :usr/uid
   :first_name :usr/first_name
   :last_name :usr/last_name
   :email :usr/email
   :VAT :usr/vat
   :phone_num :usr/phone_num
   :password :usr/password
   :country :usr/country
   :location :usr/location})

(def user-login-shape
  {:uid :usr/uid
   :password :usr/password})
