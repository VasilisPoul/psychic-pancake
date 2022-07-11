(ns psychic-pancake.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[psychic-pancake started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[psychic-pancake has shut down successfully]=-"))
   :middleware identity})
