(ns psychic-pancake.specs.decoders.image)

(defprotocol ->ImageRef
  (this->ImageRef [this] "tranforms [this] to an image url"))

(extend-protocol ->ImageRef
  Long
  (this->ImageRef [id]
    (str "/api/images/" id))
  psychic_pancake.Image
  (this->ImageRef [^psychic_pancake.Image img]
    (-> img
     .getId
     this->ImageRef))
  java.util.Map
  (this->ImageRef [^java.util.Map img]
    (-> img :id this->ImageRef))
  String
  (this->ImageRef [s] s)
  nil
  (this->ImageRef [_] nil))
