(ns psychic-pancake.handler
  (:require
    [psychic-pancake.middleware :as middleware]
    [psychic-pancake.routes.services :refer [service-routes]]
    [reitit.swagger-ui :as swagger-ui]
    [reitit.ring :as ring]
    [ring.middleware.content-type :refer [wrap-content-type]]
    [ring.middleware.webjars :refer [wrap-webjars]]
    [psychic-pancake.env :refer [defaults]]
    [mount.core :as mount]))

(mount/defstate init-app
  :start ((or (:init defaults) (fn [])))
  :stop  ((or (:stop defaults) (fn []))))

(mount/defstate app-routes
  :start
  (ring/ring-handler
    (ring/router
      [["/" {:get
             {:handler (constantly {:status 301 :headers {"Location" "/api/api-docs/index.html"}}) }}]
       (service-routes)])
    (ring/routes
      (ring/create-resource-handler
       {:path "/"})
      (wrap-content-type (wrap-webjars (constantly nil)))
      (ring/create-default-handler))))

(defn app []
  (middleware/wrap-base #'app-routes))


(comment
  
  
  


  

  (.GetUID (User.))
  (em/create-entity-manager)
  (em/create-native-query "name" User)
  (.getEntityManagerFactory (cljjpa.producers.EntityManagerFactoryProcducer.))
  (.getPersistenceProviders (javax.persistence.spi.PersistenceProviderResolverHolder/getPersistenceProviderResolver))
  (with-entity-manager
  (with-transaction
    (em/merge User {:uid "group1"
                    :email ""}))))
