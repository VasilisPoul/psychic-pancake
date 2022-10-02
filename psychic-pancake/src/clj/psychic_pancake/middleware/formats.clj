(ns psychic-pancake.middleware.formats
  (:require
    [luminus-transit.time :as time]
    [muuntaja.core :as m]
    [muuntaja.format.core :refer [EncodeToBytes] :as m.core]
    [clojure.data.xml :as xml]
    [psychic-pancake.orm.core :as orm :refer [hash-map->obj]]
    [psychic-pancake.specs.common :refer [time-formatter format-time]]
    [clojure.walk :refer [walk prewalk postwalk]]
    [java-time :as jt])
  (:import
   [psychic_pancake Listing Bid User Location Country Category]
   [java.util Map Date List]
   [clojure.lang Keyword]))


(def instance
  (m/create
    (-> m/default-options
        (update
         :formats
         #(select-keys % ["application/json"])))))

(defn- try-meta [obj meta_]
  (try
    (vary-meta obj (partial merge-with first) meta_)
    (catch Exception e obj)))

(defn- with-xml-root
  ([attrs obj]
  (if (-> obj meta :xml-root)
    (xml/element (-> obj meta :xml-root) attrs obj)
    obj))
  ([obj] (with-xml-root {} obj)))

(defprotocol IConvertToXML
  (->XML [this] "converts `this` to an xml element"))


(extend-protocol IConvertToXML
  Listing
  (->XML [this]
    (xml/element
     :Item
     {:ItemID (-> this .getItem_id str)}
     (concat
      [(xml/element :Name {} [(.getName this)])]
      (->> this
           .getCategories
           (map #(xml/element :Category {} [(->XML %)]))
           (apply vector))
      [(xml/element :Currently {}
                [(->> this .getCurrently (str "$"))])
       (xml/element :First_Bid {}
                [(->> this .getFirst_bid (str "$"))])
       (xml/element :Number_of_Bids {} [(->> this .getBids count str)])
       (xml/element :Bids {} (map ->XML (.getBids this)))
       ;; (xml/element :Location {} [(-> this .getLocation ->XML)])
       (xml/element :Country {} [(-> this .getCountry ->XML)])
       (xml/element :Started {} [(-> this .getStarted ->XML)])
       (xml/element :Ends {} [(-> this .getEnds ->XML)])
       (xml/element :Seller
                (let [seller (.getSeller this)]
                  {:Rating (-> seller .getRating str)
                   :UserID (.getUid seller)})
                [])
       (xml/element :Description {} [(.getDescription this)])])))
  Date
  (->XML [this] (format-time this))
  java.sql.Timestamp
  (->XML [this] (format-time this))
  Bid
  (->XML [this]
    (xml/element :Bid
             {}
             [(let [bidder (.getBidder this)]
                (xml/element :Bidder {:Rating (-> bidder .getRating str)
                                  :UserID (.getUid bidder)}
                         [(xml/element
                           :Location {}
                           [(-> bidder .getLocation ->XML)])
                          (xml/element
                           :Country {}
                           [(-> bidder .getCountry ->XML)])]))
              (xml/element :Time {} [(-> this .getTime ->XML)])
              (xml/element :Amount {}
                       [(->> this .getAmount (str "$"))])]))
  Location
  (->XML [this] (.getName this))
  Country
  (->XML [this] (.getName this))
  Category
  (->XML [this] (.getName this))
  Map
  (->XML [this]
    (let [orig (-> this meta :original)]
      (if orig
        (->XML orig)
        (map
         (fn [[a b]]
           (xml/element a {} (->XML (try-meta b {:name a}))))
         this))))
  Keyword
  (->XML [this]
    (name this))
  List
  (->XML [this]
    (let [name (-> this meta (:name :List))]
      (xml/element
       name
       {}
       (map ->XML this))))
  Long
  (->XML [this] (str this))
  Double
  (->XML [this] (str this))
  String
  (->XML [this] this)
  nil
  (->XML [this] nil)
  Object
  (->XML [this] (str this)))


(defn str->ByteArrayInputStream [s]
  (->> s
       (map (comp byte int))
       byte-array))



(def instance-with-xml
  (m/create
   (-> m/default-options
        (update
         :formats
         #(select-keys % ["application/json"]))
        (assoc-in
         [:formats "application/xml" :encoder]
         (reify EncodeToBytes
           (encode-to-bytes [this data charset]
             (-> data ->XML xml/emit-str str->ByteArrayInputStream)))))))


;; (->> (orm/find! Listing 1)
;;     ->XML
;;     xml/emit-str
;;     str->ByteArrayInputStream
;;     java.io.ByteArrayInputStream.
;;     slurp)


;; (-> (m/encode
;;      instance-with-xml
;;      "application/xml"
;;      (orm/find! Listing 1))
;;     slurp)
;; (xml/emit-str (->XML {:foo :bar}))

;; (xml/element :foo :bar)

