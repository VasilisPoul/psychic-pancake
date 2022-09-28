(ns psychic-pancake.routes.user
  (:require
   [ring.util.http-response :refer :all]
   [spec-tools.core :as st]
   [clojure.spec.alpha :as s]
   [psychic-pancake.specs.common :refer [resp-404able]]
   [psychic-pancake.routes.common :refer [resp-404]]
   [psychic-pancake.specs.user :as specs.user]
   [psychic-pancake.orm.user :as orm.user]
   [psychic-pancake.orm.listing :as orm.listing]
   [psychic-pancake.orm.core :as orm]
   [clojure.set :refer [rename-keys]]
   [buddy.hashers :as h])
  (:import [jakarta.persistence EntityExistsException PersistenceException]
           [psychic_pancake User Rating Rating$RatingId]))


(def user-get-handler
  (fn [req]
    (ok (-> req
            :db
            :user-ref
            orm/obj->map
            (#(assoc % :self (:uid %)))))))

(def user-routes
  ["/user"
   {:swagger {:tags ["user"]}}
   [""
    {:post
     {:parameters {:body specs.user/user-registration-shape}
      :responses {200 {:body {:user :usr/ref}}
                  409 {:body {:reason string?}}}
      :handler (fn [{{params :body} :parameters}]
                 (try
                   (ok
                    {:user
                     (-> params
                         (update :password h/derive)
                         (rename-keys
                          {:password :password_digest})
                         (assoc :pending true)
                         orm.user/create!)})
                   (catch PersistenceException e
                     (conflict
                      {:reason
                       "A user with this id already exists"}))))}}]
   ["/me"
    {:swagger {:security [{:apiAuth []}]}
     :conflicting true
     :fetch! [{:key :user-ref
               :req->id (comp :uid :identity)
               :type :user}]
     :auth? true}
     ["" {:get
          {:responses {200 {:body specs.user/user-info-shape}}
           :handler user-get-handler}}]
    ["/items-bought"
     {:get
      {:auth? :buyer
       :responses {200 {:body (s/coll-of :listing/ref)}}
       :handler (comp
                 ok
                 #(orm.listing/get-by-winner % :show_all false)
                 (memfn ^User .getUid)
                 :user-ref
                 :db)}}]]
   ["/:id"
    {:fetch! [{:key :user-ref
               :req->id (comp :id :path :parameters)
               :type :user}]
     :parameters {:path {:id :usr/uid}}
     :conflicting true}
     [""
      {:get
       {:parameters {}
        :responses {200 {:body specs.user/user-info-shape}}
        :handler user-get-handler}}]
    ["/rating"
     {:put
      {:swagger {:security [{:apiAuth []}]}
       :parameters {:body {:rate (s/and pos-int? #(< % 6))}}
       :responses {200 {:body {:user :usr/ref
                               :new-rating pos-int?}}}
       :fetch! [{:key :me
                 :req->id (comp :uid :identity)
                 :type :user}]
       :auth? true
       :handler
       (fn [{{user :user-ref me :me} :db
            {{rate :rate} :body} :parameters}]
         (let [rate (Integer. rate)
               rating-id (Rating$RatingId. me user)
               current-rating (try (->> rating-id
                                        (orm/find! Rating)
                                        .getPoints)
                                   (catch NullPointerException e
                                     0))
               new-rating (-> user
                              .getRating
                              (+ rate)
                              (- current-rating))]
           (do
             (orm/merge!
              (doto user (.setRating new-rating)))
             (orm/merge!
              (Rating. rating-id rate))
             (ok {:new-rating new-rating
                  :user user}))))}}]]])


(orm/merge! (doto  (orm/find! User "user_name") (.setRating 100)))
