(ns psychic-pancake.routes.listings
  (:require
   [ring.util.http-response :refer :all]
   [clojure.spec.alpha :as s]
   [spec-tools.core :as st]))

(def listing-spec
   {:ItemID pos-int?
    :Name string?
    :Categories [string?]
    :Currently string?
    :First-Bid string?
    :Number-of-Bids pos-int?
    :Bids [{:Bidder {:Rating pos-int?
                     :UserID string?
                     :Location string?
                     :Country string?}
            :Time string?
            :Ammount string?}]
    :Location string?
    :Country string?
    :Started string?
    :Ends string?
    :Seller {:Rating pos-int? :UserID string?}
    :Description string?})

(def routes
  ["/listings"
   [""
    {:swagger {:tags ["listing"]}
     :get {:handler (constantly (ok))
           :response {200
                      {:body {}}}}  ;get all listings with filters
     :post {:handler (constantly (ok))} ;create a new listing
     }]
   ["/:listing-id"
    {:swagger {:tags ["listing"]}
     :parameters {:path {:listing-id pos-int?}}
     :put {:handler (constantly (ok))}
     :delete {:handler (constantly (ok))}
     :get {:handler (constantly (ok))
           :responses {200 {:body listing-spec}}}}]])
