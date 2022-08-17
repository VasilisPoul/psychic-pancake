(ns psychic-pancake.middleware
  (:require
    [psychic-pancake.env :refer [defaults]]
    [psychic-pancake.config :refer [env]]
    [ring-ttl-session.core :refer [ttl-memory-store]]
    [ring.middleware.defaults :refer [site-defaults wrap-defaults]]))

(defn my-wrap-cors [handler pat]
  (fn [req]
    (let [resp (handler req)]
      (assoc-in resp
              [:headers "Access-Control-Allow-Origin"]
              pat))))

(defn wrap-base [handler]
  (-> ((:middleware defaults) handler)
      (wrap-defaults
        (-> site-defaults
            (assoc-in [:security :anti-forgery] false)
            (assoc-in  [:session :store] (ttl-memory-store (* 60 30)))))
      (my-wrap-cors "*")))
