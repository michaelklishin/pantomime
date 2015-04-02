(ns pantomime.extract
  (:require [pantomime.internal :refer :all])            
  (:import [java.io File InputStream ByteArrayInputStream]
           [org.apache.tika Tika]
           [java.net URL]
           [org.apache.tika.metadata Metadata]
           [org.apache.tika.sax BodyContentHandler]
           [org.apache.tika.parser Parser AutoDetectParser ParseContext])
  (:use [clojure.java.io :only [input-stream]]))

(defn conv-metadata [^Metadata mdata]
  (let [names (.names mdata)]
    (zipmap (map #(keyword (.toLowerCase %1)) names)
            (map #(seq (.getValues mdata %1)) names))))

(def ^{:private true} tika-class (Tika.))

(defprotocol ExtractionOps
  (parse [input] "Extract content and metadata"))

(extend-protocol ExtractionOps
  InputStream
  (parse [^InputStream ifile]
    (let [parser   (AutoDetectParser.)
          context  (ParseContext.)
          metadata (Metadata.)
          handler  (BodyContentHandler. -1)]
      (.set context Parser parser)
      (.parse parser ifile handler metadata context)
      (assoc (conv-metadata metadata) :text (.toString handler)))))
  
(extend-protocol ExtractionOps
  java.io.File
  (parse [^File file] (with-open [is (input-stream file)] (parse is))))

;; thanks
;; https://clojuredocs.org/clojure.core/xml-seq#example-542692d6c026201cdc3270ca
;; and https://nukelight.wordpress.com/2012/02/12/xml-parsing-in-clojure/
;; for hints about String->InputStream, and that parsers seem to like
;; ByteArrayInputStream rather than InputStream
(extend-protocol ExtractionOps
  String  
  (parse [^String contents]
    (parse
     (ByteArrayInputStream.
      (.getBytes (.trim contents))))))
                             
(extend-protocol ExtractionOps
  URL
  (parse [^URL url] (with-open [is (input-stream url)] (parse is))))

(extend byte-array-type  
  ExtractionOps
  {:parse (fn [^bytes input]
            (parse (ByteArrayInputStream. input)))})
