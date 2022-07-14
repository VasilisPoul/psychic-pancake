(ns psychic-pancake.routes.user
  (:require
   [ring.util.http-response :refer :all]
   [spec-tools.core :as st]
   [psychic-pancake.specs.common :refer [resp-404able]]
   [psychic-pancake.routes.common :refer [resp-404]]
   [psychic-pancake.specs.user :refer [user-info-shape]]))




(def
  users
  "hard coded users for testing"
  {"foo" {:username "foo" :email "foo@test.com" :user-type "admin"}
   "bar" {:username "bar" :email "bar@test.com" :user-type "seller"}})


(def user-routes
  ["/user"
   {:swagger {:tags ["user"]}
    :get
     {:handler (fn [{{{id :id} :query} :parameters}]
                 (if (users id)
                   (ok (users id))
                   resp-404))
      :responses (resp-404able {200 {:body user-info-shape}})
      :parameters {:query {:id :usr/uid}}}}])

