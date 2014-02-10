;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns pantomime.web
  "Contains the same functions as pantomime.mime but is Web-oriented. Apache Tika as of April 2012 cannot
   detect PNG, JPEG and other image bytes for byte arrays. However, it is not uncommon to see broken Web
   frameworks, apps and servers that serve, say, PDF files claiming that they are text/html. pantomime.web
   attempts to improve the situation by providing special MIME type detection functions that can use
   content-based detection and Content-Type header at the same time."
  (:require [pantomime.internal :refer :all])
  (:require [pantomime.mime :as mime])
  (:import [java.io File InputStream]
           [java.net URL]
           [org.apache.tika Tika]
           [org.apache.tika.mime MediaType MimeType]))

(def ^Tika detector (Tika.))
(def ^{:private true :const true}
  default-mime-type "application/octet-stream")

(defn ^{:private true}
  pick-preference
  [body-content-type headers-content-type]
  (if (= default-mime-type body-content-type)
    (or headers-content-type default-mime-type)
    body-content-type))

;;
;; API
;;

(defprotocol MIMETypeDetection
  (mime-type-of  [body headers] "Returns MIME type of given response body, headers and url. Detection will try content-based detection
                                 (magic bytes, tag detection for XML-based formats, etc) for response body as well as
                                 Content-Type header."))

(extend-protocol MIMETypeDetection
  String
  (mime-type-of
    [^String body {content-type "content-type"}]
    (pick-preference (mime/mime-type-of (.getBytes body)) content-type))

  InputStream
  (mime-type-of
    [^InputStream body {content-type "content-type"}]
    (pick-preference (mime/mime-type-of body) content-type)))

(extend byte-array-type
  MIMETypeDetection
  {:mime-type-of (fn [^bytes body {content-type "content-type"}]
                   (let [bct (mime/mime-type-of body)]
                     (pick-preference bct content-type))) })
