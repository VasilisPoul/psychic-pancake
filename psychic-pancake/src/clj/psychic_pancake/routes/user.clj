(ns psychic-pancake.routes.user
  (:require
   [ring.util.http-response :refer :all]
   [spec-tools.core :as st]
   [psychic-pancake.specs.common :refer [resp-404able]]
   [psychic-pancake.routes.common :refer [resp-404]]
   [psychic-pancake.specs.user :as specs.user]
   [psychic-pancake.orm.user :as orm.user]
   [psychic-pancake.orm.core :as orm]
   [clojure.set :refer [rename-keys]]
   [buddy.hashers :as h])
  (:import [jakarta.persistence EntityExistsException]))


(def user-routes
  ["/user"
   {:swagger {:tags ["user"]}}
   [""
    {:post
     {:parameters {:body specs.user/user-registration-shape}
      :responses {200 {:body {:user :usr/ref}}
                  409 {:body {:reason string?}}}
      :handler (fn [{{params :body} :parameters}]
                 (try
                   (ok
                    {:user
                     (-> params
                         (update :password h/derive)
                         (rename-keys
                          {:password :password_digest})
                         (assoc :pending true)
                      orm.user/create!)})
                   (catch EntityExistsException e
                     (conflict
                      {:reason
                       "A user with this id already exists"}))))}}]
   ["/:id"
    {:fetch! [{:key :user-ref
               :req->id (comp :id :path :parameters)
               :type :user}]
     :parameters {:path {:id :usr/uid}}
     :get
     {:parameters {}
      :responses {200 {:body {:role :usr/role
                              :self :usr/ref
                              :uid :usr/uid
                              :first_name :usr/first_name
                              :last_name :usr/last_name}}}
      :handler (fn [req]
                 (ok (-> req
                         :db
                         :user-ref
                         orm/obj->map
                         (#(assoc % :self (:uid %))))))}}]])

