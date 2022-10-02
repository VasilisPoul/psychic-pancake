(ns psychic-pancake.routes.admin
  (:require
   [ring.util.http-response :refer :all]
   [clojure.spec.alpha :as s]
   [psychic-pancake.orm.user :as orm.user :refer :all]
   [psychic-pancake.orm.core :as orm :refer [->clj]]
   [psychic-pancake.orm.listing :as orm.listing]
   [psychic-pancake.middleware.formats :refer [->XML instance-with-xml]]))


(declare all-listings)

(def routes
  ["/admin"
   {:swagger {:tags ["admin"] :security [{:apiAuth []}]}
    :auth? :admin}
   ["/users"
    [""
     {:get {:responses {200 {:body (s/coll-of :usr/ref)}}
            :handler (comp ok orm/->clj (fn [& _] (get-pending-users :pending false)))}}]
    ["/pending"
     {:get
      {:responses {200 {:body (s/coll-of :usr/ref)}}
       :handler
       (comp
        ok
        orm/->clj
        (fn [& _] (get-pending-users :pending true)))}
      :post
      {:parameters {:body {:uid :usr/uid :accept boolean?}}
       :handler (fn [req]
                  (do
                    ((comp
                     #(({true pending-uid->accept!
                         false uid->delete!} (:accept %))
                       (:uid %))
                     #(select-keys % [:uid :accept])
                     :body
                     :parameters) req)
                    (ok)))}}]]
   ["/listings"
    {:muuntaja instance-with-xml
     :get
     {:responses {200 {:body [{:item_id :item/id
                               :name :item/name
                               :categories (s/coll-of
                                            :item/category)
                               :currently :item/price
                               :first_bid :item/price
                               :number_of_bids integer?
                               :bids (s/coll-of :listing/bid)
                               :country :common/country-name
                               :seller :listing/seller}]}}
      :handler
      (fn [req]
        (ok (all-listings)))}}]])


(defn all-listings []
  (vary-meta
   (->> {:only_active false}
        orm.listing/search-listings 
        ->clj
        (map #(assoc % :number_of_bids (-> % :bids count)))
        (apply vector))
   merge {:name :Items}))
