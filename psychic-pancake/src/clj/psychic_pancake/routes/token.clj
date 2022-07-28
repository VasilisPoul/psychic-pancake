(ns psychic-pancake.routes.token
  (:require
   [ring.util.http-response :refer :all]
   [spec-tools.core :as st]
   [psychic-pancake.specs.common :refer [resp-404able]]
   [psychic-pancake.routes.common :refer [resp-404]]
   [psychic-pancake.specs.user :as specs.user]
   [psychic-pancake.orm.user :as orm.user]
   [psychic-pancake.orm.core :as orm]
   [buddy.hashers :as h]
   [buddy.sign.jwt :as jwt]
   [reitit.ring :as ring]))


(def secret "VERY_SECRET_MOVE_TO_ENV")

(def routes
  ["/token"
   {:swagger {:tags ["user" "login"]}}
   [""
    {:post
     {:parameters {:form specs.user/user-login-shape}
      :responses {200 {:body {:token string?}}}
      :handler (fn [{{{uid :uid pwd :password} :form} :parameters}]
                 (let [user (orm.user/get-by-id uid)]
                   (if (:valid (h/verify pwd (.getPassword_digest user)))
                     (ok {:token (str "Token " (jwt/sign {:uid uid} secret))}))))}}]
   ["/check"
    {:get
     {:swagger {:tags ["login"] :security [{:apiAuth []}]}
      :test "foo"
      :handler (fn [req] (ok {:id (-> req :identity)
                             :test (-> req
                                       ring/get-match
                                       :data
                                       ((req :request-method))
                                       :test)}))}}]])


