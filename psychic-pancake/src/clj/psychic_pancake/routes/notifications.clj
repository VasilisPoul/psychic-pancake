(ns psychic-pancake.routes.notifications
  (:require
   [ring.util.http-response :refer :all]
   [psychic-pancake.specs.decoders.listings :refer [->ListingRef]]
   [psychic-pancake.orm.notifications :refer [check get-by-id]]
   [psychic-pancake.orm.core :as orm :refer [->clj]]
   [clojure.spec.alpha :as s]
   [spec-tools.data-spec :as ds]))

(defmulti format-notification
  (comp
   (partial apply vector)
   (partial map (comp not nil?))
   (juxt :listing_ref :message_ref :bid_ref)))

(defmethod format-notification [true false false]
  [m]
  {:type :listing-notification
   :when (:displayAt m)
   :listing (:listing_ref m)
   :listing_name (-> m :listing_ref :name)
   :winner (some-> m :listing_ref :current_bid :bidder)
   :final_price (some-> m :listing_ref :currently)})

(defmethod format-notification [false true false]
  [m]
  (let [msg (:message_ref m)]
    {:type :message-notification
     :when (:displayAt m)
     :message (:msg_id msg)
     :from (:from msg)
     :subject (:subject msg)}))

(defmethod format-notification [false false true]
  [m]
  (let [bid (:bid_ref m)]
    {:type :bid-notification
     :when (:displayAt m)
     :bidder (:bidder bid)
     :bidder_uid (-> bid :bidder :uid)
     :amount (:amount bid)
     :bid bid
     :listing (:listing bid)}))



(def routes
  ["/notifications"
   {:swagger {:tags ["user" "notifications"]
              :security [{:apiAuth []}]}
    :auth? true
    :fetch! [{:key :user-ref
               :req->id (comp :uid :identity)
               :type :user}]}
   [""
    {:get
     {:parameters {}
      :responses {200 {}}
      :handler (fn [{{me :user-ref} :db}]
                 (->> (check :user me :show_all false)
                     ->clj
                     (map (partial str "/api/notifications/"))
                     ok))}}]
   ["/:id"
    {:parameters {:path {:id integer?}}
     :fetch! [{:key :notification
               :req->id (comp :id :path :parameters)
               :type :notification
               :must-own true}]
     :get
     {:responses {200 {:body {:when :common/time
                              :type keyword?
                              ;; listing notification
                              (ds/opt :listing) :listing/ref
                              (ds/opt :listing_name) :item/name
                              (ds/opt :final_price) :item/price
                              (ds/opt :winner)
                              (s/nilable :usr/ref)
                              ;; message notification
                              (ds/opt :message) :msg/ref
                              (ds/opt :from) :usr/ref
                              (ds/opt :subject) :msg/subject}}}
      :handler (comp
                ok
                format-notification
                ->clj
                :notification
                :db)}}]])
