(ns psychic-pancake.middleware.util
  (:require
   [psychic-pancake.orm.user :as orm.user]
   [ring.util.http-response :refer :all]))

(defn wrap-fetch [handler key id->obj req->id]
  (fn [req]
    (when-let [obj (some-> req req->id id->obj)]
      (handler (assoc-in req [:fetched key] obj)))))


(defn auth-matches? [required req]
  (case required
    true (contains? req :identity)
    :buyer (some-> req :identity :role (= "buyer"))
    :seller (some-> req :identity :role (= "seller"))
    :admin (some-> req :identity :role (= "admin"))))

(def check-auth-middleware
  {:name ::check-auth-middleware
   :compile (fn [route-data opts]
              (when (contains? route-data :auth?)
                (fn [handler]
                  (fn [req]
                    (if (auth-matches? (:auth? route-data) req)
                      (handler req)
                      (unauthorized
                       {:reason "Invalid token or insufficient privileges."
                        :info (if (contains? req :identity)
                                (str
                                 "This endpoint requires a token of a(n) '"
                                 (:auth? route-data)
                                 "' account.")
                                (str
                                 "Get a valid auth token using the "
                                 "/api/token endpoint and use it "
                                 "in the 'Authorization' "
                                 "header."))}))))))})

