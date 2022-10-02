(ns psychic-pancake.routes.listings
  (:require
   [ring.util.http-response :refer :all]
   [psychic-pancake.routes.listings.bids :as routes.bids]
   [psychic-pancake.specs.listings :as specs.listings]
   [psychic-pancake.specs.common :refer [parse-time]]
   [psychic-pancake.orm.listing :as orm.listing]
   [psychic-pancake.orm.notifications :refer
    [notify-listing-ends mark-seen!]]
   [psychic-pancake.orm.core :as orm]
   [psychic-pancake.orm.query-builder :refer [*doto-query*]]
   [psychic-pancake.middleware.formats :as fmt]
   [psychic-pancake.ml.matrix-factorization :refer [state uid->recommendations]]
   [clojure.spec.alpha :as s]
   [clojure.walk :refer [postwalk]]
   [spec-tools.data-spec :as ds]
   [java-time :as jt]))

(def routes
  ["/listings"
   [""
    {:swagger {:tags ["listing"]
               :security [{:apiAuth []}]}
     :get {:parameters {:query
                        specs.listings/listings-filters-shape}
           :handler (comp
                     ok
                     (fn [listings]
                       ((if (empty? listings) identity
                            #(assoc % :last (-> listings last .getEnds))) {:listings listings}))
                     (partial apply vector)
                     #(binding [*doto-query* (fn [q] (doto q (.setMaxResults 10)))]
                        (orm.listing/search-listings %))
                     #(if (contains? % :after)
                        (update % :after (comp jt/instant->sql-timestamp parse-time))
                        %)
                     :query
                     :parameters)
           :responses {200 {:body {:listings [:listing/ref]
                                   (ds/opt :last) :common/time}}}}  ;get all listings with filters
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
   ["/recommended"
    {:auth? :buyer
     :swagger {:tags ["listing"]
               :security [{:apiAuth []}]}
     :conflicting true
     :get {:responses {200 {:body (s/coll-of :listing/ref)}}
           :handler (fn [req]
                      (let [active? #(some-> %
                                             Integer/parseInt
                                             orm.listing/get-by-id
                                             .getActive)]
                        (->> (-> req :identity :uid)                                           uid->recommendations
                             (filter active?)
                             (take 5)
                             (apply vector)
                             ok)))}}]

   ["/:listing-id"
    {:swagger {:tags ["listing"]
               :security [{:apiAuth []}]}
     :conflicting true
     :parameters {:path {:listing-id pos-int?}}}
    [""
     {
      :get {:auth? false
            :fetch! [{:key :listing
                      :req->id
                      (comp :listing-id :path :parameters)
                      :type :listing}]
            :responses {200 {:body specs.listings/listing-shape}}
            :handler
            (fn [{{listing :listing} :db
                 {uid :uid} :identity}]
              (do
                (mark-seen! listing uid)
                (-> listing
                    orm/obj->map
                    ok)))}}
     [""
      {:auth? :seller
       
       :put {:fetch! [{:key :listing
                 :req->id (comp :listing-id :path :parameters)
                 :type :listing
                 :must-own true}]
             :parameters {:body
                          specs.listings/listing-update-shape}
             :responses {200 {:body specs.listings/listing-shape}
                         409 {:body {:reason string?
                                     :info string?}}}
             :handler (fn [{{listing :listing} :db
                           {params :body
                            {id :listing-id} :path} :parameters}]
                        (if (contains? params :activate)
                          (-> listing
                              orm.listing/activate!
                              orm/obj->map
                              ok)
                          (if (empty? (.getBids listing))
                            (-> listing
                                (orm.listing/update! params)
                                orm/obj->map
                                ok)
                            (conflict
                             {:reason "Cannot edit listing"
                              :info (str "Cannot edit a listing "
                                         "after bids have been "
                                         "placed.")}))))}
       :delete {:fetch! [{:key :listing
                 :req->id (comp :listing-id :path :parameters)
                 :type :listing
                 :must-own true}]
                :responses {200 {:body {:deleted :listing/ref}}}
                :handler
                (fn [{{{id :listing-id} :path} :parameters}]
                  (do
                    (orm.listing/delete-by-id id)
                    (ok {:deleted id})))}}]
     routes.bids/routes]]])
