;; Copyright (c) 2011-2026 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns pantomime.mime
  (:require [pantomime.internal :refer :all])
  (:import [java.io File InputStream]
           [java.net URL]
           [org.apache.tika Tika]
           [org.apache.tika.detect CompositeDetector]
           [org.apache.tika.mime MediaType MimeType MimeTypes
            MediaTypeRegistry MimeTypeException]))

(def ^Tika detector (Tika.))
(def ^MimeTypes registry (MimeTypes/getDefaultMimeTypes))
(def ^MediaTypeRegistry media-type-registry (.getMediaTypeRegistry registry))

;;
;; API
;;

(defprotocol MIMETypeDetection
  (mime-type-of  [input] "Returns MIME type of given input. Supports array of bytes, files, input streams and file names.
                         With file names, detector uses only file extension. With arrays of bytes and inputs that can be coerced
                         to input stream, several additional detection methods are performed (magic bytes, tag detection for XML-based formats, etc)"))

(extend-protocol MIMETypeDetection
  String
  (mime-type-of
    [^String input]
    (.detect detector input))

  File
  (mime-type-of
    [^File input]
    (.detect detector input))

  URL
  (mime-type-of
    [^URL input]
    (.detect detector input))

  InputStream
  (mime-type-of
    [^InputStream input]
    (.detect detector input)))

(extend byte-array-type
  MIMETypeDetection
  {:mime-type-of (fn [^bytes input]
                   (.detect detector input)) })

(defn ^MimeType for-name
  "Returns a MimeType for provided name, e.g. image/png"
  [^String s]
  (.forName registry s))

(defn ^String extension-for-name
  "Returns the most common known extension for provided
   MIME type name (e.g. image/jpeg) with a leading dot, e.g.
   \".jpg\""
  [^String s]
  (try
    (if-let [mt (for-name s)]
    (.getExtension mt)
    "")
    (catch MimeTypeException _
      "")))

(def ^MimeTypes mime-adder
  (->> (.getDetector detector)
       ^CompositeDetector (identity)
       (.getDetectors)
       (filter #(instance? org.apache.tika.mime.MimeTypes %))
       first))

(defn add-pattern
  "Adds a new MimeType pattern to pantomime"
  [name pattern test]
  {:pre  [(not= name (mime-type-of test))]
   :post [(= name (mime-type-of test))]}
  (when-not (nil? mime-adder)
    (let [mime-type (.forName mime-adder name)]
      (.addPattern mime-adder mime-type pattern true))))

;; I would love to know why there are two separate, completely
;; independent mechanisms for representing these kinds of objects. -djt

;; Nevertheless, coercions are handy for switching between them.

(defprotocol MimeTypeCoercions
  (as-mime-type  [x] "Turn a { String, MediaType } object into a MimeType.")
  (as-media-type [x] "Turn a { String, MimeType } object into a MediaType.")
)

(extend-protocol MimeTypeCoercions
  String
  (as-mime-type  [^String x] (try (for-name x)))
  (as-media-type [^String x] (try (.getType ^MimeType (as-mime-type x))))
  MimeType
  (as-mime-type  [^MimeType x] x)
  (as-media-type [^MimeType x] (.getType x))
  MediaType
  (as-mime-type  [^MediaType x] (for-name (.toString (.getBaseType x))))
  (as-media-type [^MediaType x] x)
)

;; don't collide with 'core/instance?'
(defn instance-of?
  "Check if the first { MIME, media } type is an instance of the second."
  [a b]
  ;; only MediaTypes can be compared this way via the MediaTypeRegistry.
  (let [^MediaType ta (as-media-type a) ^MediaType tb (as-media-type b)]
    (.isInstanceOf media-type-registry ta tb)))
