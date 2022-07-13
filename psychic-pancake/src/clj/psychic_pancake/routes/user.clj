(ns psychic-pancake.routes.user
  (:require
   [ring.util.http-response :refer :all]
   [spec-tools.core :as st]))


(defn resp-404able [resp-schema]
  (assoc resp-schema
         404 {:body {:message string?}}))

(def
  users
  "hard coded users for testing"
  {22 {:username "foo" :email "foo@test.com" :user-type "admin"}
   28 {:username "bar" :email "bar@test.com" :user-type "seller"}})


(def user-routes
  ["/user"
   {:swagger {:tags ["user"]}
    :get
     {:handler (fn [{{{id :id} :query} :parameters}]
                 (if (users id)
                   (ok (users id))
                   (not-found {:message "user not found"})))
      :responses (resp-404able {200 {:body {:username string? :email string? :user-type string?}}})
      :parameters {:query {:id pos-int?}}}}])
