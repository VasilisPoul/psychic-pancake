(ns psychic-pancake.specs.message
  (:require [psychic-pancake.specs.common :as common]
            [clojure.spec.alpha :as s]
            [spec-tools.core :as st]
            [spec-tools.data-spec :as ds]
            [spec-tools.swagger.core :refer [accept-spec]]))

(s/def :msg/subject
  (st/spec {:spec (s/and string? (comp (partial >= 50) count))
            :description "Must be a string of characters with length in [1, 50]"
            :swagger/pattern ".{1,50}"
            :json-schema/default "Subject"
            :swagger/example "About your vintage blue t-shirt."}))

(s/def :msg/body
  (st/spec {:spec (s/and string? (comp (partial >= 200) count))
            :description "Must be a string of characters with length in [1, 200]"
            :swagger/pattern ".{1,200}"
            :json-schema/default "Message body"
            :swagger/example "I wish to buy your vintage blue t-shirt"}))

(s/def :msg/ref
  (st/spec {:spec (s/and string? (partial re-matches #"/api/messages/\d+"))
            :reason "invalid message url"
            :description "/api/messages/:id"
            :swagger/pattern "/api/messages/\\d+"
            :json-schema/default "/api/messages/:id"
            :swagger/example "/api/messages/<id>"
            :decode/response #(str "/api/messages/" %2)}))

(def message-shape
  {(ds/opt :from) :usr/ref
   :to :usr/ref
   :subject :msg/subject
   :body :msg/body
   (ds/opt :timestamp) :common/time
   (ds/opt :self) :msg/ref})
