(ns psychic-pancake.routes.admin
  (:require
   [ring.util.http-response :refer :all]
   [clojure.spec.alpha :as s]
   [psychic-pancake.orm.user :as orm.user :refer :all]
   [psychic-pancake.orm.core :as orm]))


(def routes
  ["/admin"
   {:swagger {:tags ["admin"] :security [{:apiAuth []}]}
    :auth? :admin}
   ["/users"
    [""
     {:get {:responses {200 {:body (s/coll-of :usr/ref)}}
            :handler (comp ok orm/->clj (fn [& _] (get-pending-users :pending false)))}}]
    ["/pending"
     {:get
      {:responses {200 {:body (s/coll-of :usr/ref)}}
       :handler
       (comp
        ok
        orm/->clj
        (fn [& _] (get-pending-users :pending true)))}
      :post
      {:parameters {:body {:uid :usr/uid :accept boolean?}}
       :handler (comp
                 ok
                 #(({true pending-uid->accept!
                     false uid->delete!} (:accept %))
                   (:uid %))
                 #(select-keys % [:uid :accept])
                 :body
                 :parameters)}}]]])

