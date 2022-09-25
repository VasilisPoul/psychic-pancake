(ns psychic-pancake.specs.listings
  (:require
   [clojure.spec.alpha :as s]
   [spec-tools.core :as st]
   [spec-tools.data-spec :as ds]
   [psychic-pancake.specs.decoders.bid :as dec.b]
   [psychic-pancake.specs.decoders.listings :as dec.l]
   [clojure.string :as string]))

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
  (st/spec {:spec number?
            :description "Price in dollars"
            :swagger/example 12.21}))


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
  (st/spec {:spec (ds/spec ::seller
                           {:rating :usr/rating
                            :uid :usr/uid
                            :location :usr/location
                            :country :usr/country})
            :description "The user who posted the listing"
            :swagger/example {:rating 942
                              :uid "Alice"
                              :location "Alice's Shoe Factory"
                              :country "United Kingdom"}}))

(s/def :bid/time :common/time)

(s/def :bid/ref
  (st/spec {:spec string?
            :description "Bid url"
            :swagger/example "/api/listings/<listing>/bids/<bid>"
            :decode/response #(dec.b/this->BidRef %2)}))

(s/def :listing/ref
  (st/spec {:spec string?
            :description "Item ID"
            :swagger/example "/api/listings/<id>"
            :decode/response #(dec.l/this->ListingRef %2)}))

(s/def :listing/bid
  (ds/spec {:bidder :usr/ref
            :time :common/time
            :ammount :item/price}))


(def listing-shape
   {:item_id :item/id
    :name :item/name
    :categories (s/coll-of :item/category)
    :currently :item/price
    :current_bid (s/nilable :bid/ref)
    :first_bid :item/price
    :bids (s/coll-of :bid/ref)
    :location :usr/location
    :country :usr/country
    :started :common/time
    :ends :common/time
    :seller :usr/ref
    :description string?
    :active boolean?
    :images (s/coll-of :common/image-ref)})


(def listings-filters-shape
  {(ds/opt :name) :item/name
   (ds/opt :categories) (st/spec {:spec (s/coll-of :item/category)
                                  :decode/string #(string/split %2 #",")})
   (ds/opt :price_min) (st/spec {:spec :item/price
                                 :description "Minimum Price in dollars"})
   (ds/opt :price_max) (st/spec {:spec :item/price
                                 :description "Maximum Price in dollars"})
   (ds/opt :country) :common/country-name
   (ds/opt :seller_uid) :usr/uid
   (ds/opt :seller_rating) :usr/rating
   (ds/opt :radius) (s/nilable number?)
   (ds/opt :position_lon) number?
   (ds/opt :position_lat) number?
   (ds/opt :only_active) boolean?})

(def listing-post-shape
  {:name :item/name
   :description string?
   :categories [:item/category]
   :first_bid :item/price
   :ends :listing/ends
   :images [:common/image]
   ;; :location :usr/location
   })

(def listing-update-shape
  (->> (-> listing-post-shape
           (dissoc :images)
           (assoc (ds/opt :activate)))
       (map (juxt (comp ds/opt first) second))
       (into {})))
