(ns psychic-pancake.ml.matrix-factorization
  (:require [clojure.string :as string]
            [uncomplicate.neanderthal.native :refer
             [fv factory-by-type fge]]
            [uncomplicate.neanderthal.core :refer :all]
            [uncomplicate.neanderthal.math :as scalar]
            [uncomplicate.neanderthal.vect-math :as vect]
            [uncomplicate.neanderthal.random :refer
             [rand-uniform!]]
            [neanderthal-stick.experimental :as stick.exp]
            [uncomplicate.fluokitten.core :refer [fmap fmap!]]
            [psychic-pancake.ml.data :as data]
            [clojure.core.async :refer [go-loop thread <!]])
  (:import clojure.lang.MapEntry))

(def state (atom {:pred {} :u->idx {} :idx->i {}}))

(defn rand-matrix [M N]
  (doto (fge M N)
    (rand-uniform!)))

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


(defn train [loss loss-backward lr data {IF :item-features
                                         UF :user-features
                                         :as state}]
  (let [prediction (mm UF (trans IF))
        error (loss data prediction)
        grads (loss-backward error data prediction UF IF)
        cur_lr (-> state (:iteration 0) lr)
        weight-updates [(axpy! cur_lr (first grads) UF)
                        (axpy! cur_lr (second grads) IF)]]
    {:error error
     :user-features (first weight-updates)
     :item-features (second weight-updates);; (weight-update :user-features)
     :iteration (-> state (:iteration 0) inc)}))

(defn train-epochs [train-fn epochs state]
  (let [result (nth (iterate train-fn state) epochs)]
    (print
     (format "Iteration: %d\tLoss: %f\n"
             (:iteration result 0) (:error result)))
    result))

(defn lr [it]
  (cond (< it 200) 2.5
        :else 0.1))

(defn loss-deltas [train-seq]
  (map
   #(assoc %2 :error-delta (- (:error %1 0) (:error %2 0)))
   train-seq
   (rest train-seq)))


(defn train-loop [feature-num data iteration-set-len tol]
  (let [users-num (-> data :buyer->index count)
        items-num (-> data :item->index count)
        user-features (rand-matrix users-num feature-num)
        item-features (rand-matrix items-num feature-num)
        R (:user-items-matrix data)]
    (first
     (drop-while
      #(> (:error-delta % 100) tol)
      (loss-deltas
       (iterate
        (partial train-epochs
                 (partial train MSE MSE-backward lr R)
                 iteration-set-len)
        {:user-features user-features
         :item-features item-features}))))))

;; (mm (:user-features state) (trans (:item-features state)))

(defn add-user [N user-features]
  (let [M (mrows user-features)
        mat (fge (inc M) N)]
    (copy! user-features (submatrix mat 0 0 M N))
    (copy! (rand-matrix 1 N) (submatrix mat M 0 1 N))
    mat))

(defn R-add-user [R]
  (let [mat (fmap (constantly -1) (fge (inc (mrows R)) (ncols R)))]
    (copy! R (submatrix mat 0 0 (mrows R) (ncols R)))
    mat))

(defn train-and-save! []
  (let [UF-path "/root/ebay-data/user-features.bin"
        IF-path "/root/ebay-data/item-features.bin"
        pred-path "/root/ebay-data/predictions.bin"
        user->idx-path "/root/ebay-data/user->idx.edn"
        item->idx-path "/root/ebay-data/item->idx.edn"
        data (data/dataset (data/path->maps data/data-path))
        {UF :user-features
         IF :item-features}
        (train-loop 10 data 10 0.01)]
    (swap! state assoc :idx->i (into {}
                                     (map (fn [[a b]] [b a])
                                          (data :item->index))))
    (swap! state assoc :u->idx (data :buyer->index))
    (swap! state assoc :pred (mm UF (trans IF)))))

(defn train-thread []
  (thread
    (print "Started training")
    (data/save-xml-from-db!
     "/root/ebay-data/items-db.xml")
    (time (train-and-save!))
    (Thread/sleep (* 2 60 60 1000))))

(defn uid->recommendations [uid]
  (when (and (-> uid keyword ((@state :u->idx)))
             (@state :idx->i)
             (@state :pred))
      (map (comp (@state :idx->i) second)
           (sort-by (comp - first)
                    (map
                     vector
                     (row (:pred @state) (-> uid
                                             keyword
                                             ((@state :u->idx))))
                     (range))))))

(train-thread)
