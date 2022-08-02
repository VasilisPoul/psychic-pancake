(ns psychic-pancake.specs.common
  (:require [clojure.spec.alpha :as s]
            [spec-tools.core :as st]
            [java-time]))

(defn resp-404able [resp-schema]
  (assoc resp-schema
         404 {:body {:message (st/spec {:spec string?
                                        :swagger/pattern "404 - Resource not found"
                                        :swagger/example "404 - Resource not found"})}}))

(s/def :common/country-name
  #{"Afghanistan" "Albania" "Algeria" "Andorra" "Angola"
    "Antigua & Deps" "Argentina" "Armenia" "Australia" "Austria"
    "Azerbaijan" "Bahamas" "Bahrain" "Bangladesh" "Barbados"
    "Belarus" "Belgium" "Belize" "Benin" "Bhutan"
    "Bolivia" "Bosnia Herzegovina" "Botswana" "Brazil" "Brunei"
    "Bulgaria" "Burkina" "Burundi" "Cambodia" "Cameroon"
    "Canada" "Cape Verde" "Central African Rep" "Chad" "Chile"
    "China" "Colombia" "Comoros" "Congo" "Congo {Democratic Rep}"
    "Costa Rica" "Croatia" "Cuba" "Cyprus" "Czech Republic"
    "Denmark" "Djibouti" "Dominica" "Dominican Republic" "East Timor"
    "Ecuador" "Egypt" "El Salvador" "Equatorial Guinea" "Eritrea"
    "Estonia" "Ethiopia" "Fiji" "Finland" "France"
    "Gabon" "Gambia" "Georgia" "Germany" "Ghana"
    "Greece" "Grenada" "Guatemala" "Guinea" "Guinea-Bissau"
    "Guyana" "Haiti" "Honduras" "Hungary" "Iceland"
    "India" "Indonesia" "Iran" "Iraq" "Ireland {Republic}"
    "Israel" "Italy" "Ivory Coast" "Jamaica" "Japan"
    "Jordan" "Kazakhstan" "Kenya" "Kiribati" "Korea North"
    "Korea South" "Kosovo" "Kuwait" "Kyrgyzstan" "Laos"
    "Latvia" "Lebanon" "Lesotho" "Liberia" "Libya"
    "Liechtenstein" "Lithuania" "Luxembourg" "Macedonia" "Madagascar"
    "Malawi" "Malaysia" "Maldives" "Mali" "Malta"
    "Marshall Islands" "Mauritania" "Mauritius" "Mexico" "Micronesia"
    "Moldova" "Monaco" "Mongolia" "Montenegro" "Morocco"
    "Mozambique" "Myanmar, {Burma}" "Namibia" "Nauru" "Nepal"
    "Netherlands" "New Zealand" "Nicaragua" "Niger" "Nigeria"
    "Norway" "Oman" "Pakistan" "Palau" "Panama"
    "Papua New Guinea" "Paraguay" "Peru" "Philippines" "Poland"
    "Portugal" "Qatar" "Romania" "Russian Federation" "Rwanda"
    "St Kitts & Nevis" "St Lucia"
    "Saint Vincent & the Grenadines" "Samoa" "San Marino"
    "Sao Tome & Principe" "Saudi Arabia" "Senegal" "Serbia" "Seychelles"
    "Sierra Leone" "Singapore" "Slovakia" "Slovenia" "Solomon Islands"
    "Somalia" "South Africa" "South Sudan" "Spain" "Sri Lanka"
    "Sudan" "Suriname" "Swaziland" "Sweden" "Switzerland"
    "Syria" "Taiwan" "Tajikistan" "Tanzania" "Thailand"
    "Togo" "Tonga" "Trinidad & Tobago" "Tunisia" "Turkey"
    "Turkmenistan" "Tuvalu" "Uganda" "Ukraine" "United Arab Emirates"
    "United Kingdom" "United States" "Uruguay" "Uzbekistan" "Vanuatu"
    "Vatican City" "Venezuela" "Vietnam" "Yemen" "Zambia" "Zimbabwe"})


(def time-formatter (-> "LLL-dd-yy HH:mm:ss"
                        java-time/formatter 
                        (.withZone  (java-time/zone-id))))

(def format-time (comp (partial java-time/format time-formatter)
                       java-time/local-date-time
                       java-time/instant->sql-timestamp))
(def parse-time (partial java-time/java-date time-formatter))


(s/def :common/time
  (st/spec
   {:spec (s/and string? (partial re-matches #"\w\w\w-\d\d-\d\d \d\d:\d\d:\d\d"))
    :description "time"
    :decode/json #(format-time %2)
    :decode/response #(format-time %2)}))

;; (st/coerce
;;  :common/time
;;  1659051982906
;;  st/json-transformer)
