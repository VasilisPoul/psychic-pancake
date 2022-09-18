(ns psychic-pancake.routes.install
  (:require
   [ring.util.http-response :refer :all]
   [clojure.spec.alpha :as s]
   [psychic-pancake.orm.user :as orm.user :refer :all]
   [psychic-pancake.orm.country :refer [create-countries!]]
   [psychic-pancake.orm.core :as orm]
   [psychic-pancake.middleware.install :refer [matches?]]
   [buddy.hashers :refer [derive]])
  (:import [psychic_pancake User User$Role Country]))


(defn- installed? []
  (try
    (do (orm/find! User "") true)
    (catch Exception e
      (if (matches? e)
        false
        (throw e)))))


(defn install! [params]
  (do
    (orm/with-session
      (orm/with-transaction
        (-> orm/*session*
            (.createNativeQuery "CREATE SCHEMA IF NOT EXISTS main")
            .executeUpdate)))
    (orm/make-session-factory!)
    (create-countries!)
    (-> params
        (assoc :role User$Role/admin)
        (assoc :password_digest (-> :password params derive))
        (update :country (partial orm/find! Country))
        (orm/hash-map->obj User)
        orm/persist!)))

(def routes
  ["/install"
   {:swagger {:tags ["install"]}
    :post
    {:parameters {:body {:uid :usr/uid
                         :password :usr/password
                         :email :usr/email
                         :country :usr/country}}
     :handler (fn [{{body :body} :parameters}]
                (if (not (installed?))
                  (do (install! body) (ok))
                  (gone
                   {:reason "Installation already in place"
                    :info "This application is already installed. Try accessing the other endpoints."})))}
    :get
    {:handler (constantly (ok {:installed (installed?)}))}}])
