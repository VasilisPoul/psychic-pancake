(ns psychic-pancake.routes.user
  (:require
   [ring.util.http-response :refer :all]
   [spec-tools.core :as st]
   [psychic-pancake.specs.common :refer [resp-404able]]
   [psychic-pancake.routes.common :refer [resp-404]]
   [psychic-pancake.specs.user :as specs.user]
   [psychic-pancake.orm.user :as orm.user]
   [psychic-pancake.orm.core :as orm]
   [buddy.hashers :as h]))


(def user-routes
  ["/user"
   {:swagger {:tags ["user"]}}
   [""
    {:post
     {:parameters {:body specs.user/user-registration-shape}
      :responses {200 {:body {:user :usr/ref}}}
      :handler (fn [{{params :body} :parameters}]
                 (ok
                  (orm.user/create!
                   (dissoc
                    (assoc params
                           :password_digest (h/derive (params :password))
                           :pending true)
                    :password))))}}]
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

