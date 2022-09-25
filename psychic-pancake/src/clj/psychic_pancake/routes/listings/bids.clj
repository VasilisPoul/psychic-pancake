(ns psychic-pancake.routes.listings.bids
  (:require
   [ring.util.http-response :refer :all]
   [psychic-pancake.specs.listings :as specs.listings]
   [psychic-pancake.orm.listing :as orm.listing]
   [psychic-pancake.orm.notifications :refer [notify-new-bid]]
   [psychic-pancake.orm.bid :as orm.bid]
   [psychic-pancake.orm.core :as orm]
   [clojure.spec.alpha :as s]
   [spec-tools.data-spec :as ds]
   [clojure.set :refer [rename-keys]])
  (:import
   [psychic_pancake Listing]))


(def routes
  ["/bids"                              
   {:swagger {:tags ["bid"]}}
   [""
    {:swagger {:security [{:apiAuth []}]}
     :post {:auth? :buyer
            :fetch! [{:type :user
                      :req->id (comp :uid :identity)
                      :key :bidder}]
            :parameters {:body
                         {:amount :item/price}}
            :responses {200 {:bid :bid/ref}}
            :handler (fn [{{body :body} :parameters
                          db :db}]
                       (cond
                         (not (.isActive ^Listing (:listing db)))
                         (not-acceptable {:reason "Invalid bid attempted"
                                          :info "The listing is not currently active"})
                         (<= (:amount body)
                             (.getCurrently ^Listing (:listing db)))
                         {:status 402
                          :body {:reason "Invalid bid attempted"
                                 :info "Bidding amount must be higher than current price"}}
                         :else
                         (let [bid
                               (-> body
                                   (merge db)
                                   (select-keys
                                    [:amount :listing :bidder])
                                   orm.bid/create!)]
                           (do
                             (notify-new-bid bid)
                             (ok {:bid bid})))))}}]
   ["/:bid-id"
    {:parameters {:path {:bid-id pos-int?}}
     :fetch! [{:type :bid
               :req->id (comp :bid-id :path :parameters)
               :key :bid}]
     :get {:responses {200 {:body {:amount :item/price
                                   :bidder :usr/ref
                                   :listing :listing/ref
                                   :time :common/time
                                   :self :bid/ref}}}
           :handler
           (fn [{{bid :bid
                 listing :listing} :db}]
             (-> bid
                 orm/obj->map
                 (assoc :self bid)
                 ok))}}]])

