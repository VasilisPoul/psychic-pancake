(ns psychic-pancake.middleware.user
  (:require
   [ring.util.http-response :refer :all]
   [psychic-pancake.orm.user :as orm.user]))

(def ref-user-middleware
  {:name ::ref-user-middleware
   :compile
   (fn [route-data opts]
     (when-let [ref-users (:ref-users route-data)]
       (fn [handler]
         (fn [req]
           (let [users (map (fn [{k :key f :req->uid :as m}]
                              (assoc m
                                     :uid (f req)
                                     :usr
                                     (-> req
                                         f orm.user/get-by-id)))
                            ref-users)
                 wrap-users #(reduce
                              (fn [m {k :key v :usr}]
                                (assoc m k v))
                              % users)
                 user-nil? (comp nil? :usr)]
             (if (every? (complement user-nil?) users)
               (-> req wrap-users handler)
               (let [invalid-uids (->> users
                                       (filter user-nil?)
                                       (map :uid))]
                 (not-found
                  {:reason "Not Found"
                   :info (str
                          "Referenced user ids"
                          " "
                          (clojure.string/join ", " invalid-uids)
                          " do not correspond to"
                          " existing users")}))))))))})


