(ns psychic-pancake.specs.decoders.listings)

(defprotocol ->ListingRef
  (this->ListingRef [this] "tranforms [this] to a listing url"))

(extend-protocol ->ListingRef
  Long
  (this->ListingRef [listing-id]
    (str "/api/listings/" listing-id))
  psychic_pancake.Listing
  (this->ListingRef [^psychic_pancake.Listing l]
    (this->ListingRef
     (.getItem_id l)))
  java.util.Map
  (this->ListingRef [l-map]
    (this->ListingRef (:item_id l-map)))
  String
  (this->ListingRef [s] s)
  nil
  (this->ListingRef [_] nil))
