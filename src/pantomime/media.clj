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
