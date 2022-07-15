(ns psychic-pancake.routes.listings
  (:require
   [ring.util.http-response :refer :all]
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
  (st/spec {:spec string?
            :description "Price in dollars"
            :swagger/example "$12.21"}))

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

(s/def :bid/time string?)

(def listing-spec
   {:ItemID :item/id
    :Name :item/name
    :Categories [:item/category]
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


(def listings-filters-spec
  {(ds/opt :Name) :item/name
   (ds/opt :Categories) [:item/category]
   (ds/opt :Price-Min) (st/spec {:spec :item/price
                                 :description "Minimum Price in dollars"})
   (ds/opt :Price-Max) (st/spec {:spec :item/price
                                 :description "Maximum Price in dollars"})
   (ds/opt :Location) :usr/location
   (ds/opt :Country) :usr/country
   (ds/opt :Started) :listing/started
   (ds/opt :Ends) :listing/ends
   (ds/opt :Seller-Rating) :usr/rating})

(def routes
  ["/listings"
   [""
    {:swagger {:tags ["listing"]}
     :get {:parameters {:query listings-filters-spec}
           :handler (constantly (ok))
           :responses {200 {:body [:item/id]}}}  ;get all listings with filters
     :post {:handler (constantly (ok))} ;create a new listing
     }]
   ["/:listing-id"
    {:swagger {:tags ["listing"]}
     :parameters {:path {:listing-id pos-int?}}
     :put {:handler (constantly (ok))}
     :delete {:handler (constantly (ok))}
     :get {:handler (constantly (ok))
           :responses {200 {:body listing-spec}}}}]])
