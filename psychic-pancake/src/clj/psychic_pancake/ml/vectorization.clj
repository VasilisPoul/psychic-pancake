(ns psychic-pancake.ml.vectorization
  (:require [clojure.string :as string]
            [uncomplicate.neanderthal.native :refer [dv]]
            [uncomplicate.neanderthal.core :refer [mm]])
  (:import clojure.lang.MapEntry))

(def bow-dictionary
  (-> "https://www.mit.edu/~ecprice/wordlist.10000"
      slurp
      (string/split-lines)
      ((partial map vector) (range))
      flatten
      ((partial apply sorted-map))))

(def empty-bow
  (apply vector (for [[word idx] bow-dictionary] 0)))

(defn tokens [s]
  (filter
   #(contains? bow-dictionary %)
   (-> s
       (string/split #"[ \n\t.!-_'\"]"))))

(defn tokens->vector [tokens]
  (dv
   (let [freqs (frequencies tokens)]
     (for [[word idx] bow-dictionary]
       (get freqs word 0)))))

(defn clean-text [text]
  (->> text
       string/lower-case
       (filter #(or (Character/isLetter %) (Character/isSpace %)))
       (apply str)
       (#(string/replace % #"\s+" " "))))

(def vectorize (comp tokens->vector tokens clean-text))


(let [example "brand new beautiful handmade european blown glass ornament from christopher radko. this particular ornament features a snowman paired with a little girl bundled up in here pale blue coat sledding along on a silver and blue sled filled with packages. the ornament is approximately 5_ tall and 4_ wide. brand new and never displayed, it is in its clear plastic packaging and comes in the signature black radko gift box. PLEASE READ CAREFULLY!!!! payment by cashier's check, money order, or personal check. personal checks must clear before shipping. the hold period will be a minimum of 14 days. I ship with UPS and the buyer is responsible for shipping charges. the shipping rate is dependent on both the weight of the package and the distance that package will travel. the minimum shipping/handling charge is $6 and will increase with distance and weight. shipment will occur within 2 to 5 days after the deposit of funds. a $2 surcharge will apply for all USPS shipments if you cannot have or do not want ups service. If you are in need of rush shipping, please let me know and I_will furnish quotes on availability. the BUY-IT-NOW price includes free domestic shipping (international winners and residents of alaska and hawaii receive a credit of like value applied towards their total) and, as an added convenience, you can pay with paypal if you utilize the feature. paypal is not accepted if you win the auction during the course of the regular bidding-I only accept paypal if the buy it now feature is utilized. thank you for your understanding and good luck! Free Honesty Counters powered by Andale! Payment Details See item description and Payment Instructions, or contact seller for more information. Payment Instructions See item description or contact seller for more information."]
  (vectorize example))

