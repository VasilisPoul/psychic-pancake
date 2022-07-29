(ns psychic-pancake.middleware.util
  (:require
   [psychic-pancake.orm.user :as orm.user]))

(defn wrap-fetch [handler key id->obj req->id]
  (fn [req]
    (when-let [obj (some-> req req->id id->obj)]
      (handler (assoc-in req [:fetched key] obj)))))
