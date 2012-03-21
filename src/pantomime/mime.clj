(ns pantomime.mime
  (:import [java.io File InputStream]
           [java.net URL]
           [org.apache.tika Tika]
           [org.apache.tika.mime MediaType MimeType]))

(def ^Tika detector (Tika.))

;; clojure.java.io has these as private, so we had to copy them. MK.
(def ^{:doc "Type object for a Java primitive byte array."}
  byte-array-type (class (make-array Byte/TYPE 0)))

(def ^{:doc "Type object for a Java primitive char array."}
  char-array-type (class (make-array Character/TYPE 0)))

;;
;; API
;;

(defprotocol MIMETypeDetection
  (mime-type-of  [path] "Returns MIME type of file at given path or with given filename"))

(extend-protocol MIMETypeDetection
  String
  (mime-type-of
    [^String path]
    (.detect detector path))

  File
  (mime-type-of
    [^File path]
    (.detect detector path))

  URL
  (mime-type-of
    [^URL path]
    (.detect detector path))

  InputStream
  (mime-type-of
    [^InputStream is]
    (.detect detector is)))

(extend byte-array-type
  MIMETypeDetection
  {:mime-type-of (fn [^bytes input]
                   (.detect detector input)) })