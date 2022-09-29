(ns psychic-pancake.ml.matrix-factorization
  (:require [clojure.string :as string]
            [uncomplicate.neanderthal.native :refer
             [dv factory-by-type dge]]
            [uncomplicate.neanderthal.core :refer :all]
            [uncomplicate.neanderthal.math :as scalar]
            [uncomplicate.neanderthal.vect-math :as vect]
            [uncomplicate.neanderthal.random :refer
             [rand-uniform!]]
            [uncomplicate.fluokitten.core :refer [fmap fmap!]])
  (:import clojure.lang.MapEntry))


(def feature-num 30)
(def users-num 100)
(def items-num 250)



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





(defn delta [Data Pred]
  (fmap #(if (= -1 %1) 0 %2) Data (axpy -1 Data Pred)))

(defn MSE [Data Pred]
  (->> Pred
       (delta Data)
       vect/sqr!
       (scal! (/ 1 (* 2 (dim Data))))
       sum))

(defn MSE-backward [err Data Pred UF IF]
  (let [D (delta Data Pred)
        a (- (/ 2 (dim Pred)))]
    [(->> IF
          (mm D)
          (scal a))
     (->> UF
          (mm (trans D))
          (scal a))]))


(defn train [loss loss-backward lr data {IF :item-features UF :user-features :as state}]
  (let [prediction (mm UF (trans IF))
        error (loss data prediction)
        grads (loss-backward error data prediction UF IF)
        cur_lr (-> state (:iteration 0) lr)
        weight-updates [(axpy cur_lr (first grads) UF)
                        (axpy cur_lr (second grads) IF)]]
    {:error error
     :user-features (first weight-updates)
     :item-features (second weight-updates);; (weight-update :user-features)
     :iteration (-> state (:iteration 0) inc)}))

(def R (vect/abs (rand-matrix users-num items-num)))
(def user-features (rand-matrix users-num feature-num))
(def item-features (rand-matrix items-num feature-num))

(defn train-epochs [train-fn epochs state]
  (let [result (nth (iterate train-fn state) epochs)]
    (print
     (format "Iteration: %d\tLoss: %f\n" (:iteration result 0) (:error result)))
    result))

;; (let [best (apply min-key :error
;;                   (take 100
;;                         (iterate (partial train MSE MSE-backward (constantly 1e-3) R)
;;                                  {:user-features user-features
;;                                   :item-features item-features
;;                                   :error (MSE R (mm user-features item-features))})))]
;;   (:error best))

(defn lr [it]
  (cond (< it 10) 0.1
        :else 0.1))

(defn loss-deltas [train-seq]
  (map
   #(assoc %2 :error-delta (- (:error %1 0) (:error %2 0)))
   train-seq
   (rest train-seq)))

(def state (last
            (take-while
             #(> (:error-delta %) 1e-3)
             (loss-deltas
              (iterate
               (partial train-epochs (partial train MSE MSE-backward lr R) 10)
               {:user-features user-features
                :item-features item-features
                :error (MSE R (mm user-features (trans item-features)))})))))

(mm (:user-features state) (trans (:item-features state)))

(defn add-user [N user-features]
  (let [M (mrows user-features)
        mat (dge (inc M) N)]
    (copy! user-features (submatrix mat 0 0 M N))
    (copy! (rand-matrix 1 N) (submatrix mat M 0 1 N))
    mat))

(defn R-add-user [R]
  (let [mat (fmap (constantly -1) (dge (inc (mrows R)) (ncols R)))]
    (copy! R (submatrix mat 0 0 (mrows R) (ncols R)))
    mat))



(let [UF (add-user feature-num (:user-features state))
      IF (-> state :item-features)
      R (R-add-user R)]
  (->> {:user-features UF
        :item-features IF
        :error 100}
       (iterate (partial train MSE MSE-backward lr R))
       loss-deltas
       (take-while #(> (:error-delta %) 1e-3))
       last))

(state :error)

