(ns psychic-pancake.specs.decoders.user)

(defprotocol ->UserRef
  (this->UserRef [this] "tranforms [this] to a user url"))

(extend-protocol ->UserRef
  String
  (this->UserRef [^String s] (str "/api/user/" s))
  psychic_pancake.User
  (this->UserRef [^psychic_pancake.User u]
    (-> u
        .getUid
        this->UserRef))
  java.util.Map
  (this->UserRef [u-map] (-> u-map
                             :uid
                             this->UserRef))
  nil
  (this->UserRef [_] nil))

(defprotocol ICountryName
  (->CountryName [this]))

(extend-protocol ICountryName
  String
  (->CountryName [this] this)
  psychic_pancake.Country
  (->CountryName [this] (.getName this))
  java.util.Map
  (->CountryName [this] (:name this))
  nil
  (->CountryName [_] nil))
