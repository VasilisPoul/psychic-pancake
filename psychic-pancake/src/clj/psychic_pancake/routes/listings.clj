(ns psychic-pancake.routes.listings
  (:require
   [ring.util.http-response :refer :all]
   [psychic-pancake.routes.listings.bids :as routes.bids]
   [psychic-pancake.specs.listings :as specs.listings]
   [psychic-pancake.orm.listing :as orm.listing]
   [psychic-pancake.orm.core :as orm]
   [clojure.spec.alpha :as s]
   [clojure.walk :refer [postwalk]]
   [spec-tools.data-spec :as ds]))


(defn transform-listing [listing]
  (-> listing
      (update :country :name)
      (assoc :location "test")))


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
                         specs.listings/listing-post-shape}
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
                     orm.listing/create!))}))} ;create a new listing
     }]
   ["/:listing-id"
    {:swagger {:tags ["listing"]}
     :parameters {:path {:listing-id pos-int?}}
     :fetch! [{:key :listing
               :req->id (comp :listing-id :path :parameters)
               :type :listing}]}
    [""
     {:put {:parameters {:body
                        specs.listings/listing-update-shape}
           :responses {200 {:body specs.listings/listing-shape}}
           :handler (fn [{{listing :listing} :db
                         {params :body
                          {id :listing-id} :path} :parameters}]
                      (-> listing
                          orm/obj->map
                          (merge params)
                          orm.listing/create!
                          orm/obj->map
                          transform-listing
                          ok))}
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
                 transform-listing
                 ok))}}]
    routes.bids/routes]])
