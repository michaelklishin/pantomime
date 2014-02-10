;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
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
           [org.apache.tika.mime MediaType MimeType]))

(def ^Tika detector (Tika.))

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
