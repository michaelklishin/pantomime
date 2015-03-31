(ns pantomime.extract
  (:import [java.io File InputStream]
           [org.apache.tika Tika]
           [java.net URL]
           [org.apache.tika.metadata Metadata]
           [org.apache.tika.sax BodyContentHandler]
           [org.apache.tika.parser Parser AutoDetectParser ParseContext])
  (:require [byte-streams :as bs])
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
  
;; XXX this is, sadly, not working
(extend-protocol ExtractionOps
  String
  (parse [^String contents] (parse (bs/to-input-stream contents))))
                             
(extend-protocol ExtractionOps
  URL
  (parse [^URL url] (with-open [is (input-stream url)] (parse is))))
