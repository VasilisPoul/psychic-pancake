(ns psychic-pancake.routes.messages
  (:require
   [ring.util.http-response :refer :all]
   [spec-tools.core :as st]
   [clojure.spec.alpha :as s]
   [psychic-pancake.specs.common :refer [resp-404able]]
   [psychic-pancake.routes.common :refer [resp-404]]
   [psychic-pancake.specs.user :as specs.user]
   [psychic-pancake.orm.user :as orm.user]
   [psychic-pancake.orm.message :as orm.message]
   [psychic-pancake.orm.core :as orm]
   [buddy.hashers :as h]
   [psychic-pancake.ownership :refer [owns?]]
   [psychic-pancake.specs.message :refer [message-shape]]
   [java-time :as time])
  (:import
   (psychic_pancake Message User)))




(defn mailbox-route [path getter]
  [path
   {:conflicting true
    :get
     {:responses {200 {:body (s/coll-of :msg/ref)}}
      :parameters {}
      :handler (fn [req]
                 (ok
                  (map
                   (fn [^Message msg] (.getMsg_id msg))
                    (-> req :identity :uid orm.user/get-by-id getter))))}}])


(def routes
  ["/messages"
   {:swagger {:tags ["messages"] :security [{:apiAuth []}]}
    :auth? true}
   [""
    {:post
     {:parameters {:body {:to :usr/uid
                          :subject :msg/subject
                          :body :msg/body}}
      :responses {200 {:body {:url :msg/ref}}
                  422 {:body nil?}}
      :handler (fn [req]
                 (if-let [msg (orm.message/create!
                               (-> req
                                   :parameters
                                   :body
                                   (assoc :from (-> req :identity :uid))))]
                   (-> msg .getMsg_id ((fn [x] {:url x})) ok)))}}]
   (mailbox-route "/inbox" (fn [^User u] (.getInbox u)))
   (mailbox-route "/outbox" (fn [^User u] (.getOutbox u)))
   ["/:id"
    {:conflicting true
     :get
     {:parameters {:path {:id int?}}
      :responses {200 {:body message-shape}}
      :handler
      (fn [req]
        (let [me (-> req :identity :uid)
              msg (-> req
                      :parameters
                      :path
                      :id
                      orm.message/get-by-id
                      orm/obj->map
                      (clojure.set/rename-keys {:msg_id :self}))]
          (ok msg)))}}]])
