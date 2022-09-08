(ns psychic-pancake.routes.services
  (:require
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    [reitit.ring.coercion :as coercion]
    [reitit.coercion.spec]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.multipart :as multipart]
    [reitit.ring.middleware.parameters :as parameters]
    [psychic-pancake.middleware.formats :as formats]
    [psychic-pancake.middleware.util :as util]
    [psychic-pancake.middleware.orm :as mw.orm]
    [psychic-pancake.middleware.install :refer [wrap-installed?]]
    [ring.util.http-response :refer :all]
    [clojure.java.io :as io]
    [psychic-pancake.routes.user :as user]
    [psychic-pancake.routes.listings :as listings]
    [psychic-pancake.routes.token :as token]
    [psychic-pancake.routes.messages :as messages]
    [psychic-pancake.routes.images :as images]
    [psychic-pancake.routes.notifications :as notifications]
    [psychic-pancake.routes.admin :as admin]
    [psychic-pancake.routes.install :as install]
    [buddy.auth.middleware]
    [buddy.auth.backends.token]
    [spec-tools.core :as st]
    [spec-tools.transform :as stt]
    [psychic-pancake.specs.responses :as specs.responses]))


(def coercion
  (-> reitit.coercion.spec/default-options
      (assoc-in
       ,,,
       [:transformers :response :default]
       (st/type-transformer
        {:name :response
         :decoders (merge
                    stt/json-type-decoders
                    stt/strip-extra-keys-type-decoders)
         :encoders stt/json-type-encoders
         :default-encoder stt/any->any}))
      reitit.coercion.spec/create))

(defn service-routes []
  ["/api"
   {:coercion coercion
    :muuntaja formats/instance
    :swagger {:id ::api
              :securityDefinitions {:apiAuth {:type "apiKey"
                                              :name "authorization"
                                              :in "header"}}}
    :responses {401 specs.responses/status-401-shape
                404 specs.responses/status-404-shape
                503 specs.responses/status-503-shape}
    :middleware [;; query-params & form-params
                 parameters/parameters-middleware
                 ;; content-negotiation
                 muuntaja/format-negotiate-middleware
                 ;; encoding response body
                 muuntaja/format-response-middleware
                 ;; exception handling
                 coercion/coerce-exceptions-middleware
                 ;; decoding request body
                 muuntaja/format-request-middleware
                 ;; coercing response bodys
                 coercion/coerce-response-middleware
                 ;; coercing request parameters
                 coercion/coerce-request-middleware
                 ;; multipart
                 multipart/multipart-middleware
                 #(buddy.auth.middleware/wrap-authentication
                   %
                   (buddy.auth.backends.token/jws-backend
                    {:secret token/secret}))
                 wrap-installed?
                 util/check-auth-middleware
                 mw.orm/db-fetch-middleware]}
   
   ;; swagger documentation
   ["" {:no-doc true
        :swagger {:info {:title "my-api"
                         :description "https://cljdoc.org/d/metosin/reitit"}}}

    ["/swagger.json"
     {:get {:handler (swagger/create-swagger-handler)}}]

    ["/api-docs/*"
     {:get (swagger-ui/create-swagger-ui-handler
             {:url "/api/swagger.json"
              :config {:validator-url nil}})}]]

   ["/ping"
    {:get (constantly (ok {:message "pong"}))}]
   
   user/user-routes
   listings/routes
   token/routes
   messages/routes
   images/routes
   notifications/routes
   admin/routes
   install/routes

   ["/test"
    {:swagger {:tags ["default"]}
     :get {:handler (constantly (ok {}))}}]
   
   ;; ["/math"
   ;;  {:swagger {:tags ["math"]}}

   ;;  ["/plus"
   ;;   {:get {:summary "plus with spec query parameters"
   ;;          :parameters {:query {:x int?, :y int?}}
   ;;          :responses {200 {:body {:total pos-int?}}}
   ;;          :handler (fn [{{{:keys [x y]} :query} :parameters}]
   ;;                     {:status 200
   ;;                      :body {:total (+ x y)}})}
   ;;    :post {:summary "plus with spec body parameters"
   ;;           :parameters {:body {:x int?, :y int?}}
   ;;           :responses {200 {:body {:total pos-int?}}}
   ;;           :handler (fn [{{{:keys [x y]} :body} :parameters}]
   ;;                      {:status 200
   ;;                       :body {:total (+ x y)}})}}]]

   ;; ["/files"
   ;;  {:swagger {:tags ["files"]}}

   ;;  ["/upload"
   ;;   {:post {:summary "upload a file"
   ;;           :parameters {:multipart {:file multipart/temp-file-part}}
   ;;           :responses {200 {:body {:name string?, :size int?}}}
   ;;           :handler (fn [{{{:keys [file]} :multipart} :parameters}]
   ;;                      {:status 200
   ;;                       :body {:name (:filename file)
   ;;                              :size (:size file)}})}}]

   ;;  ["/download"
   ;;   {:get {:summary "downloads a file"
   ;;          :swagger {:produces ["image/png"]}
   ;;          :handler (fn [_]
   ;;                     {:status 200
   ;;                      :headers {"Content-Type" "image/png"}
   ;;                      :body (-> "public/img/warning_clojure.png"
   ;;                                (io/resource)
   ;;                                (io/input-stream))})}}]]
   ])
