(ns psychic-pancake.middleware
  (:require
    [psychic-pancake.env :refer [defaults]]
    [psychic-pancake.config :refer [env]]
    [ring-ttl-session.core :refer [ttl-memory-store]]
    [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
    [ring.middleware.cors :refer [wrap-cors]]))

(defn wrap-base [handler]
  (-> ((:middleware defaults) handler)
      (wrap-defaults
        (-> site-defaults
            (assoc-in [:security :anti-forgery] false)
            (assoc-in  [:session :store] (ttl-memory-store (* 60 30)))))
      (wrap-cors :access-control-allow-origin #".*"
                 :access-control-allow-methods #{:get :post :put :delete}
                 :access-control-allow-headers #{"Content-Type"
                                                 "Accept"})))
