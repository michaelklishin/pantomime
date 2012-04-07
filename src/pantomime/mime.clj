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
