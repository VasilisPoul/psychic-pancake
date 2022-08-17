(ns psychic-pancake.specs.decoders.bid)

(defprotocol ->BidRef
  (this->BidRef [this] "tranforms [this] to a bid url"))

(extend-protocol ->BidRef
  clojure.lang.PersistentVector
  (this->BidRef [[^Long listing-id
                  ^Long bid-id ]]
    (str "/api/listings/" listing-id "/bids/" bid-id))
  psychic_pancake.Bid
  (this->BidRef [^psychic_pancake.Bid b]
    (this->BidRef
     [(-> b
         (.getListing)
         (.getItem_id))
      (.getId b)]))
  java.util.Map
  (this->BidRef [b-map]
    ((juxt :listing :id) b-map)))
