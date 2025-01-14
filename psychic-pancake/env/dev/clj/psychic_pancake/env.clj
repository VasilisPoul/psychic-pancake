(ns psychic-pancake.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [psychic-pancake.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[psychic-pancake started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[psychic-pancake has shut down successfully]=-"))
   :middleware wrap-dev})
