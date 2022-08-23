(ns psychic-pancake.routes.images
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
   [reitit.ring :as ring])
  (:import [psychic_pancake Image]))


(def routes
  ["/images"
   
   ["/:id"
    {:parameters {:path {:id int?}}
     :fetch! [{:key :image
               :req->id (comp :id :path :parameters)
               :type :image}]
     :swagger {:tags ["images" "listing"]}
     :get
     {:handler (comp
                ok
                (partial hash-map :image)
                (memfn ^Image getB64)
                :image
                :db)}}]])
