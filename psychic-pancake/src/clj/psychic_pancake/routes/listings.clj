(ns psychic-pancake.routes.listings
  (:require
   [ring.util.http-response :refer :all]
   [psychic-pancake.specs.listings :refer [listings-filters-shape
                                           listing-shape]]
   [psychic-pancake.orm.listing :as orm.listing]
   [clojure.spec.alpha :as s]))





(def routes
  ["/listings"
   [""
    {:swagger {:tags ["listing"]
               :security [{:apiAuth []}]}
     :get {:parameters {:query listings-filters-shape}
           :handler (constantly (ok))
           :responses {200 {:body [:item/id]}}}  ;get all listings with filters
     :post {:fetch! [{:key :user-ref
                      :req->id (comp :uid :identity)
                      :type :user}]
            :responses {200 {:body {:listing :listing/ref}}}
            :parameters {:body
                         {:name :item/name
                          :description string?
                          :categories (s/coll-of :item/category)
                          :first_bid :item/price
                          :ends :listing/ends}}
            :handler
            (fn [{{body :body} :parameters
                 {usr :user-ref} :db}]
              (ok
               {:listing
                (.getItem_id
                 (-> body
                     (assoc :seller usr)
                     (assoc :location (.getLocation usr))
                     (assoc :country (.getCountry usr))
                     (assoc :currently (:first_bid body))
                     orm.listing/create!))}))} ;create a new listing
     }]
   ["/:listing-id"
    {:swagger {:tags ["listing"]}
     :parameters {:path {:listing-id pos-int?}}
     :fetch! []
     :put {:handler (constantly (ok))}
     :delete {:handler (constantly (ok))}
     :get {:responses {200 {:body listing-shape}}
           :handler
           (fn [req]
             )}}]])

