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
     {:parameters {:form specs.user/user-registration-shape}
      :responses {200 {:body specs.user/user-registration-shape}}
      :handler (fn [{{params :form} :parameters}]
                 (ok
                  (orm/obj->map
                   (orm.user/create!
                    (dissoc
                     (assoc params
                            :password_digest (h/derive (params :password))
                            :pending true)
                     :password)))))}}]
   ["/:id"
    {:ref-users [{:key :user-ref
                  :req->uid (comp :id :path :parameters)}]
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
                         :user-ref
                         orm/obj->map
                         (select-keys
                          [:role :uid :first_name :last_name])
                         (#(assoc % :self (:uid %))))))}}]])

