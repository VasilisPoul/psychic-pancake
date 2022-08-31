(ns psychic-pancake.routes.admin
  (:require
   [ring.util.http-response :refer :all]
   [psychic-pancake.orm.user :as orm.user :refer :all]
   [psychic-pancake.orm.core :as orm]))


(def routes
  ["/admin"
   {:swagger {:tags ["admin"]}}
   ["/users"
    ["/pending"
     {:responses {200 {:body [:usr/ref]}}
      :get
      {:handler
       (comp
        ok
        orm/->clj
        get-pending-users)}
      :post
      {:handler (comp
                 ok
                 #(({true pending-uid->accept!
                     false uid->delete!} (:accept %))
                   (:uid %))
                 #(select-keys % [:uid :accept])
                 :body
                 :parameters)}}]]])

