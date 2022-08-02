(ns psychic-pancake.specs.listings
  (:require
   [clojure.spec.alpha :as s]
   [spec-tools.core :as st]
   [spec-tools.data-spec :as ds]))

(s/def :item/id
  (st/spec {:spec int?
            :description "Item ID"
            :swagger/example 123456789}))

(s/def :item/name
  (st/spec {:spec string?
            :description "Item Name"
            :json-schema/default "Brown Shoes, mint condition"
            :swagger/example "Brown Shoes, mint condition"}))

(s/def :item/category
  (st/spec {:spec string?
            :description "Item Category"
            :json-schema/default "Shoes"
            :swagger/example "Shoes"}))

(s/def :item/price
  (st/spec {:spec double?
            :description "Price in dollars"
            :swagger/example 12.21
            :decode/json
            #((comp
               parse-double
               second
               (partial re-matches
                        #"\$((([123456789]\d*)|0)(\.\d+)?)"))
              %2)}))




(s/def :bid/bidder
  (st/spec {:spec {:Rating :usr/rating
                   :UserID :usr/uid
                   :Location :usr/location
                   :Country :usr/country}
            :description "The user who placed this bid"
            :swagger/example {:Rating 227
                              :UserID "Bob"
                              :Location "Bob's Home"
                              :Country "United States"}}))

(s/def :listing/started
  (st/spec {:spec :common/time
            :description "Time at which this bid was placed"
            :swagger/example "Dec-11-01 17:57:26"}))

(s/def :listing/ends
  (st/spec {:spec :common/time
            :description "Time at which this bid will be over"
            :swagger/example "Dec-19-01 00:00:00"}))

(s/def :listing/poster
  (st/spec {:spec {:Rating :usr/rating
                   :UserID :usr/uid
                   :Location :usr/location
                   :Country :usr/country}
            :description "The user who posted the listing"
            :swagger/example {:Rating 942
                              :UserID "Alice"
                              :Location "Alice's Shoe Factory"
                              :Country "United Kingdom"}}))

(s/def :bid/time :common/time)

(s/def :listing/ref
  (st/spec {:spec string?
            :description "Item ID"
            :swagger/example "/api/listings/<id>"
            :decode/response #(str "/api/listings/" %2)}))


(def listing-shape
   {:ItemID :item/id
    :Name :item/name
    :Categories (s/coll-of :item/category)
    :Currently :item/price
    :First-Bid :item/price
    :Number-of-Bids pos-int?
    :Bids [{:Bidder :bid/bidder
            :Time :bid/time
            :Ammount :item/price}]
    :Location :usr/location
    :Country :usr/country
    :Started :listing/started
    :Ends :listing/ends
    :Seller :listing/poster
    :Description string?})


(def listings-filters-shape
  {(ds/opt :name) :item/name
   (ds/opt :categories) [:item/category]
   (ds/opt :price-min) (st/spec {:spec :item/price
                                 :description "Minimum Price in dollars"})
   (ds/opt :price-max) (st/spec {:spec :item/price
                                 :description "Maximum Price in dollars"})
   (ds/opt :location) :usr/location
   (ds/opt :country) :usr/country
   (ds/opt :started) :listing/started
   (ds/opt :ends) :listing/ends
   (ds/opt :seller-rating) :usr/rating})

