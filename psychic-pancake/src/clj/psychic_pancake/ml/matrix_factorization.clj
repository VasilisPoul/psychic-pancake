(ns psychic-pancake.ml.matrix-factorization
  (:require [clojure.string :as string]
            [uncomplicate.neanderthal.native :refer
             [dv factory-by-type]]
            [uncomplicate.neanderthal.core :refer
             [mm mrows ncols trans axpy]]
            [uncomplicate.neanderthal.random :refer
             [rand-uniform!]]
            [uncomplicate.fluokitten.core :refer [fmap fmap!]])
  (:import clojure.lang.MapEntry))

(def R
  (-> [[5 3 0 1]
       [4 0 0 1]
       [1 1 0 5]
       [1 0 0 4]
       [0 1 5 4]]
      dv
      trans))

(def feature-num 8)
(def users-num 5)
(def items-num 4)



(defn rand-matrix [M N]
  (let [zrow (->> (cycle [0])
                  (take M)
                  (apply vector))
        zmat (->> (cycle [zrow])
                  (take N)
                  (apply vector))]
    (doto
        (dv zmat)
      (rand-uniform!))))


(defn MSE [Data Pred]
  (fmap! (fn ^Double [^Double x] (* x x))
         (axpy -1 Data Pred)))

(defrecord grad-history [g])

(let [user-features (rand-matrix users-num feature-num)
      item-features (rand-matrix feature-num items-num)]
  (MSE R (mm user-features item-features)))
