(ns psychic-pancake.middleware.formats
  (:require
    [luminus-transit.time :as time]
    [muuntaja.core :as m]))

(def instance
  (m/create
    (-> m/default-options
        (update
         :formats
         #(select-keys % ["application/json"])))))
