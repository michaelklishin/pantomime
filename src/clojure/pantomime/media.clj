;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns pantomime.media
  (:import [org.apache.tika.mime MediaType]))

;;
;; Implementation
;;

(def ^{:const true :private true}
  application-media-type "application")
(def ^{:const true :private true}
  text-media-type "text")
(def ^{:const true :private true}
  image-media-type "image")
(def ^{:const true :private true}
  audio-media-type "audio")
(def ^{:const true :private true}
  video-media-type "video")
(def ^{:const true :private true}
  multipart-media-type "multipart")

(def ^{:const true :private true}
  charset-key "charset")



;;
;; API
;;

(defn media-type-named
  [^String name]
  (MediaType/parse name))

(def TEXT_PLAIN        MediaType/TEXT_PLAIN)
(def TEXT_HTML         (MediaType/parse "text/html"))
(def APPLICATION_XHTML (MediaType/parse "application/xhtml+xml"))
(def APPLICATION_XML   MediaType/APPLICATION_XML)
(def APPLICATION_JSON  (MediaType/parse "application/json"))

(defprotocol MediaTypeOps
  (base-type [input] "Returns base media type for given input, for example, text/html for text/html; charset=UTF-8")
  (parameters-of [input] "Returns optional parameters map for given input")
  (has-parameters? [input] "Returns true if given content type has parameters, false otherwise")
  (charset-of [input] "Returns optional charset given input, for example, UTF-8 for text/html; charset=UTF-8 and nil for text/html")
  (application? [input] "Returns true if input is an application/* media type, false otherwise")
  (text?  [input] "Returns true if input is a text/* media type, false otherwise")
  (image? [input] "Returns true if input is a image/* media type, false otherwise")
  (audio? [input] "Returns true if input is a audio/* media type, false otherwise")
  (video? [input] "Returns true if input is a video/* media type, false otherwise")
  (multipart? [input] "Returns true if input is a multipart/* media type, false otherwise")
  (^org.apache.tika.mime.MediaType parse [input] "Parses the given input into a media type"))


(extend-protocol MediaTypeOps
  String
  (base-type
    [^String input]
    (.getBaseType ^MediaType (media-type-named input)))
  (parameters-of
    [^String input]
    (parameters-of (parse input)))
  (has-parameters?
    [^String input]
    (has-parameters? (parse input)))
  (charset-of
    [^String input]
    (charset-of (parse input)))
  (parse
    [^String input]
    (MediaType/parse input))
  (multipart?
    [^String input]
    (multipart? (base-type input)))
  (application?
    [^String input]
    (application? (base-type input)))
  (text?
    [^String input]
    (text? (base-type input)))
  (audio?
    [^String input]
    (audio? (base-type input)))
  (video?
    [^String input]
    (video? (base-type input)))
  (image?
    [^String input]
    (image? (base-type input)))


  MediaType
  (base-type
    [^MediaType input]
    (.getBaseType input))
  (parameters-of
    [^MediaType input]
    (when-let [jm (.getParameters input)]
      (into {} jm)))
  (has-parameters?
    [^MediaType input]
    (.hasParameters input))
  (charset-of
    [^MediaType input]
    (when-let [m (parameters-of input)]
      (get m charset-key)))
  (parse
    [^MediaType input]
    input)
  (multipart?
    [^MediaType input]
    (= (.getType input) multipart-media-type))
  (application?
    [^MediaType input]
    (= (.getType input) application-media-type))
  (text?
    [^MediaType input]
    (= (.getType input) text-media-type))
  (audio?
    [^MediaType input]
    (= (.getType input) audio-media-type))
  (video?
    [^MediaType input]
    (= (.getType input) video-media-type))
  (image?
    [^MediaType input]
    (= (.getType input) image-media-type)))
