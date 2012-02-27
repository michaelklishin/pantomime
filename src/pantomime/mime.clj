(ns pantomime.mime
  (:import [java.io File InputStream]
           [java.net URL]
           [org.apache.tika Tika]
           [org.apache.tika.mime MediaType MimeType]))

(def ^Tika detector (Tika.))


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
