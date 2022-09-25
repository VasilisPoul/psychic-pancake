(ns psychic-pancake.routes.listings
  (:require
   [ring.util.http-response :refer :all]
   [psychic-pancake.routes.listings.bids :as routes.bids]
   [psychic-pancake.specs.listings :as specs.listings]
   [psychic-pancake.orm.listing :as orm.listing]
   [psychic-pancake.orm.notifications :refer
    [notify-listing-ends mark-seen!]]
   [psychic-pancake.orm.core :as orm]
   [psychic-pancake.middleware.formats :as fmt]
   [clojure.spec.alpha :as s]
   [clojure.walk :refer [postwalk]]
   [spec-tools.data-spec :as ds]))


(def routes
  ["/listings"
   [""
    {:swagger {:tags ["listing"]
               :security [{:apiAuth []}]}
     :get {:parameters {:query
                        specs.listings/listings-filters-shape}
           :handler (comp
                     ok
                     (partial apply vector)
                     orm.listing/search-listings
                     :query
                     :parameters)
           :responses {200 {:body [:listing/ref]}}}  ;get all listings with filters
     :post {:auth? :seller
            :fetch! [{:key :user-ref
                      :req->id (comp :uid :identity)
                      :type :user}]
            :responses {200 {:body map?}}
            :parameters {:body
                         specs.listings/listing-post-shape}
            :handler
            (fn [{{body :body} :parameters
                 {usr :user-ref} :db}]
              (let [listing
                    (-> body
                        (assoc :seller usr)
                        (assoc :location (.getLocation usr))
                        (assoc :country (.getCountry usr))
                        orm.listing/create!)]
                (do
                  (notify-listing-ends listing)
                  (ok {:listing listing}))))}}]
   ["/:listing-id"
    {:swagger {:tags ["listing"]
               :security [{:apiAuth []}]}
     :parameters {:path {:listing-id pos-int?}}
     :fetch! [{:key :listing
               :req->id (comp :listing-id :path :parameters)
               :type :listing}]}
    [""
     {:put {:auth? :seller
            :parameters {:body
                        specs.listings/listing-update-shape}
            :responses {200 {:body specs.listings/listing-shape}
                        409 {:body {:reason string?
                                    :info string?}}}
            :handler (fn [{{listing :listing} :db
                         {params :body
                          {id :listing-id} :path} :parameters}]
                      (if (empty? (.getBids listing))
                        (-> listing
                          orm/obj->map
                          (merge params)
                          orm.listing/create!
                          orm/obj->map
                          ok)
                        (conflict
                         {:reason "Cannot edit listing"
                          :info (str "Cannot edit a listing "
                                     "after bids have been "
                                     "placed.")})))}
      :delete {:auth? :seller
               :responses {200 {:body {:deleted :listing/ref}}}
               :handler
               (fn [{{{id :listing-id} :path} :parameters}]
                 (do
                   (orm.listing/delete-by-id id)
                   (ok {:deleted id})))}
      :get {:responses {200 {:body specs.listings/listing-shape}}
            :handler
            (fn [{{listing :listing} :db}]
              (do
                (mark-seen! listing)
                (-> listing
                    orm/obj->map
                    ok)))}}]
    routes.bids/routes]])

