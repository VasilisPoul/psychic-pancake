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


(defn wrap-referenced-user [handler]
  (fn [req]
    (handler (assoc req
                    :user-ref (orm.user/get-by-id (-> req :parameters :path :id))))))


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
    {:middleware [wrap-referenced-user]
     :get
     {:handler (fn ;; [{{{id :id} :path} :parameters user :user-ref}]
                 [req]
                 (ok (select-keys (orm/obj->map (:user-ref req))
                                  [:role :uid :first_name :last_name])))
     ;; :responses (resp-404able {200 {:body specs.user/user-info-shape}})
     :parameters {:path {:id :usr/uid}}}}]])
