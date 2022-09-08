(ns psychic-pancake.middleware.install
  (:require
   [ring.util.http-response :refer :all])
  (:import
   jakarta.persistence.PersistenceException
   org.hibernate.HibernateException))

(def not-installed-error
  (service-unavailable
   {:reason "The service is not installed properly"
    :info "Install the application by creating an admin account to proceed"}))

(def pattern-matches?
  (partial re-matches #"ERROR: relation \".*\" does not exist\n  Position: .*"))

(defprotocol PInstallationException
  (->Message [e] "Get the installation error message from this exception")
  (matches? [e] "true iff e is indication we have not installed"))

(extend-protocol PInstallationException
  PersistenceException
  (->Message [e] (-> e .getCause ->Message))
  (matches? [e] (-> e ->Message pattern-matches?))
  HibernateException
  (->Message [e] (-> e .getSQLException .getMessage ))
  (matches? [e] (-> e ->Message pattern-matches?))
  Object
  (->Message [e] "")
  (matches? [e] false))


(defn wrap-installed? [handler]
  (fn [request]
    (try
      (handler request)
      (catch Exception e
        (if (matches? e)
          not-installed-error
          (throw e))))))
