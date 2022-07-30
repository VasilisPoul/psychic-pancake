(ns psychic-pancake.middleware.util
  (:require
   [psychic-pancake.orm.user :as orm.user]
   [ring.util.http-response :refer :all]))

(defn wrap-fetch [handler key id->obj req->id]
  (fn [req]
    (when-let [obj (some-> req req->id id->obj)]
      (handler (assoc-in req [:fetched key] obj)))))

(def check-auth-middleware
  {:name ::check-auth-middleware
   :compile (fn [route-data opts]
              (when (:auth? route-data)
                (fn [handler]
                  (fn [req]
                    (if (contains? req :identity)
                      (handler req)
                      (unauthorized
                       {:reason "Invalid token"
                        :info (str
                               "Get a valid auth token using the "
                               "/api/token endpoint and use it "
                               "in the 'Authorization' "
                               "header.")}))))))})

