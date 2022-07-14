(ns psychic-pancake.routes.common
  (:require
   [ring.util.http-response :refer :all]))

(def resp-404
  (not-found {:message "404 - Resource not found"}))
