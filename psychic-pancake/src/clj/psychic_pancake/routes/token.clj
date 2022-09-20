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
     {:parameters {:body specs.user/user-login-shape}
      :responses {200 {:body {:token string?}}}
      :handler (fn [{{{uid :uid pwd :password} :body} :parameters}]
                 (let [user (orm.user/get-by-id uid)]
                   (if (:valid (h/verify pwd (.getPassword_digest user)))
                     (ok {:token (str "Token " (jwt/sign {:uid uid :role (-> user .getRole str)} secret))})
                     (unauthorized
                      {:reason "Invalid username or password"
                       :info "Create an account or check your credentials"}))))}}]
   ["/check"
    {:get
     {:swagger {:tags ["login"] :security [{:apiAuth []}]}
      :auth? true
      :fetch! [{:type :user
                :key :me
                :req->id (comp :uid :identity)}]
      :responses {200 {:body {:role :usr/role
                              :uid :usr/uid
                              :first_name :usr/first_name
                              :last_name :usr/last_name}}}
      :handler (fn [{{me :me} :db}]
                 (-> me
                     orm/obj->map
                     ok))}}]])


