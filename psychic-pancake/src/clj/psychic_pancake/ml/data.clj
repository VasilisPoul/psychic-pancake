(ns psychic-pancake.ml.data
  (:require [clojure.string :as string]
            [uncomplicate.neanderthal.native :refer [dv dge]]
            [uncomplicate.fluokitten.core :refer [fmap! ]]
            [uncomplicate.neanderthal.core :refer [entry!]]
            [clojure.java.io :refer [resource]]
            [clojure.data.xml :as xml]
            [org.satta.glob :refer [glob]]
            [clojure.string :as string]
            [clojure.edn :as edn]
            [clojure.data.priority-map :refer [priority-map]]
            [psychic-pancake.ml.vectorization :refer [vectorize]])
  (:import clojure.lang.MapEntry))

(def data-path "/root/ebay-data/items-*.xml")

(defn read-data [pattern]
  (flatten (map (comp :content xml/parse-str slurp) (glob pattern))))

(defn with-tag [tag]
  (comp (partial = tag) :tag))

(defn item->map [x]
  (let [content (:content x)
        id (-> x :attrs :ItemID)]
    {:description
     (some->> content (filter (with-tag :Description)) first :content first vectorize)
     :bidders
     (->> content
          (filter (with-tag :Bids))
          first
          :content
          (filter (with-tag :Bid))
          (map (comp keyword :UserID :attrs first (partial filter (with-tag :Bidder)) :content)))
     :item_id id}))

(defn buyers [item-maps]
  (apply priority-map (interleave (->> item-maps (map :bidders) flatten set) (range))))

(defn listings [item-maps]
  (apply priority-map (interleave (map :item_id item-maps) (range))))

(defn R-matrix [buyers listings item-maps]
  (let [mat (fmap! (constantly -1)
                   (dge (count buyers) (count listings)))]
    (doseq [im item-maps]
      (when-let [j (-> im :item_id listings)]
        (doseq [bidder (:bidders im)]
          (when-let [i (-> bidder buyers)]
              (entry! mat i j 1)))))
    mat))

(def path->maps
  (comp (partial map item->map) read-data))

(defn dataset [item-maps]
  (let [buyer->index (buyers item-maps)
        item->index (listings item-maps)]
    {:index->description (zipmap (map (comp item->index :item_id) item-maps)
                                 (map :description item-maps))
     :buyer->index buyer->index
     :item->index item->index
     :user-items-matrix (R-matrix buyer->index item->index item-maps)}))



;; (time )
;; (defn sorted-map-by-value [& key-vals]
;;   )

;; {:description #RealBlockVector[double, n:10000, offset: 0, stride:1]
;; [   6.00    0.00    0.00    ⋯      0.00    0.00 ]
;; , :bidders (), :item_id "1043374545"}
