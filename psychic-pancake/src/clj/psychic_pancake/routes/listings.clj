(ns psychic-pancake.routes.listings
  (:require
   [ring.util.http-response :refer :all]
   [psychic-pancake.specs.listings :as specs.listings]
   [psychic-pancake.orm.listing :as orm.listing]
   [psychic-pancake.orm.core :as orm]
   [clojure.spec.alpha :as s]))





(def routes
  ["/listings"
   [""
    {:swagger {:tags ["listing"]
               :security [{:apiAuth []}]}
     :get {:parameters {:query
                        specs.listings/listings-filters-shape}
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
     :fetch! [{:key :listing
               :req->id (comp :listing-id :path :parameters)
               :type :listing}]
     :put {:handler (constantly (ok))}
     :delete {:responses {200 {:body {:deleted :listing/ref}}}
              :handler
              (fn [{{{id :listing-id} :path} :parameters}]
                (do
                  (orm.listing/delete-by-id id)
                  (ok {:deleted id})))}
     :get {:responses {200 {:body specs.listings/listing-shape}}
           :handler
           (fn [{{listing :listing} :db}]
             (-> listing
                 orm/obj->map
                 (update :country :name)
                 (assoc :location "test")
                 (update :seller #(select-keys %
                                  [:uid
                                   :rating
                                   :location
                                   :country]))
                 (assoc-in [:seller :rating] 100)
                 (assoc-in [:seller :location] "test")
                 (update-in [:seller :country] :name)
                 ok))}}]])

