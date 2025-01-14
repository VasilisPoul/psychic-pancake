(ns psychic-pancake.middleware.orm
  (:require
   [ring.util.http-response :refer :all]
   [psychic-pancake.orm.core :as orm]
   [psychic-pancake.orm.user :as user]
   [psychic-pancake.orm.message :as message]
   [psychic-pancake.orm.listing :as listing]
   [psychic-pancake.orm.image :as image]
   [psychic-pancake.orm.bid :as bid]
   [psychic-pancake.orm.notifications :as notification]
   [psychic-pancake.ownership :refer [owns?]]))

(def type->db-fn
  {:user user/get-by-id
   :message message/get-by-id
   :listing listing/get-by-id
   :bid bid/get-by-id
   :image image/get-by-id
   :notification notification/get-by-id})

(def type->404-msg
  (fn [t]
    #(str "Resources with ids "
          (clojure.string/join ", " %)
          " of type '" (name t) "' do not exist.")))

(defn- assoc-fetches [req fetches]
  (reduce
   (fn [m {k :key v :ent}]
     (assoc-in m [:db k] v))
   req fetches))

(defn- found? [fetch]
  (not (nil? (:ent fetch))))

(defn do-fetch! [req
                 {k :key
                  t :type
                  f :req->id :as m}]
  (let [id (f req)
        ent ((type->db-fn t) id)
        must-own (get m :must-own false)]
    (assoc m
           :id (f req)
           :ent
           (when (or (not must-own)
                     (owns? (-> req :identity :uid)
                            ent))
             ent))))


(def db-fetch-middleware
  {:name ::db-fetch-middleware
   :compile
   (fn [route-data opts]
     (when-let [fetch-map (:fetch! route-data)]
       (fn [handler]
         (fn [req]
           (let [fetches (map (partial do-fetch! req) fetch-map)]
             (if (every? found? fetches)
               (-> req (assoc-fetches fetches) handler)
               (let [groups (->> fetches
                                 (filter (comp not found?))
                                 (group-by :type))]
                 (not-found
                  {:reason "Not Found"
                   :info
                   (map (fn [[type ents]]
                          (->> ents
                               (map :id)
                               ((type->404-msg type))))
                        groups)}))))))))})
