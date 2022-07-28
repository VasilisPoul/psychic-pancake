(ns psychic-pancake.routes.messages
  (:require
   [ring.util.http-response :refer :all]
   [spec-tools.core :as st]
   [psychic-pancake.specs.common :refer [resp-404able]]
   [psychic-pancake.routes.common :refer [resp-404]]
   [psychic-pancake.specs.user :as specs.user]
   [psychic-pancake.orm.user :as orm.user]
   [psychic-pancake.orm.core :as orm]
   [buddy.hashers :as h]
   [psychic-pancake.ownership :refer [owns?]]))




;; (def routes
;;   ["/messages"
;;    {:swagger {:tags ["user"]}}
;;    [""
;;     {:get
;;      {:responses
;;       :handler}
;;      :post}]])
