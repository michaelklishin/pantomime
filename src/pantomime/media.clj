(ns pantomime.media
  (:import [org.apache.tika.mime MediaType]))


;;
;; API
;;

(defn media-type-named
  [^String name]
  (MediaType/parse name))

(def TEXT_PLAIN       MediaType/TEXT_PLAIN)
(def TEXT_HTML        (MediaType/parse "text/html"))
(def APPLICATION_XML  MediaType/APPLICATION_XML)
(def APPLICATION_JSON (MediaType/parse "application/json"))

(defprotocol BaseMediaType
  (base-type [input] "Returns base media type for given input, for example, text/html for text/html; charset=UTF-8"))

(extend-protocol BaseMediaType
  String
  (base-type
    [^String input]
    (.getBaseType ^MediaType (media-type-named input)))

  MediaType
  (base-type
    [^MediaType input]
    (.getBaseType input)))
