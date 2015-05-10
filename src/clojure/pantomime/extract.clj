(ns pantomime.extract
  (:require [pantomime.internal :refer :all]
            [clojure.java.io :refer [input-stream copy]])
  (:import [java.io File InputStream ByteArrayInputStream]
           [org.apache.tika Tika]
           [java.net URL]
           [org.apache.tika.metadata Metadata]
           [org.apache.tika.sax BodyContentHandler XHTMLContentHandler]
           [org.apache.tika.parser Parser AbstractParser
            AutoDetectParser ParseContext]))

(defn conv-metadata
  [^Metadata mdata]
  (let [names (.names mdata)]
    (zipmap (map #(keyword (.toLowerCase ^String %1)) names)
            (map #(seq (.getValues mdata ^String %1)) names))))

(def ^{:private true} tika-class (Tika.))

(def ^{:private true} autodetectparser (AutoDetectParser.))

(defn extract-parser
  "Parser for embedded documents"
  [embedded-meta]
  (proxy [AbstractParser] []
    (getSupportedTypes [context]
      (.getSupportedTypes ^Parser autodetectparser context))
    (parse [stream handler metadata context]
      (let [tmp-fh           (File/createTempFile "pantomime-" "-embedded")
            meta             {:path (.getPath ^File tmp-fh)
                              :name (.get ^Metadata metadata "resourceName")}]
        (do
          (copy stream tmp-fh)
          (swap! embedded-meta conj meta))))))

(defprotocol ExtractionOps
  (parse [input] [input ex] "Extract content and metadata.")
  (parse-extract-embedded [input] "Extract content and metadata,
  saving any embedded documents as temp files, returning paths to
  saved files in metadata"))

(defn do-parse
  [ifile ex]
  (let [parser   (AutoDetectParser.)
        context  (ParseContext.)
        metadata (Metadata.)
        handler  (BodyContentHandler. -1)
        embedded-meta (atom [])]
    (if ex
      (do
        (.set context Parser (extract-parser embedded-meta))
        (.parse parser ifile handler metadata context)
        (assoc (conv-metadata metadata)
               :text     (.toString handler)
               :embedded @embedded-meta))
      (do
        (.set context Parser parser)
        (.parse parser ifile handler metadata context)
        (assoc (conv-metadata metadata)
               :text     (.toString handler))))))                          

(extend-protocol ExtractionOps
  InputStream
  (parse [^InputStream ifile] (do-parse ifile false))
  (parse-extract-embedded [^InputStream ifile] (do-parse ifile true)))
     
(extend-protocol ExtractionOps
  java.io.File
  (parse [^File file] (with-open [is (input-stream file)] (parse is)))
  (parse-extract-embedded [^File file]
    (with-open [is (input-stream file)] (parse-extract-embedded is))))
    
(extend-protocol ExtractionOps
  String
  (parse [^String filename]
    (with-open [is (input-stream filename)] (parse is)))
  (parse-extract-embedded [^String filename]
    (with-open [is (input-stream filename)] (parse-extract-embedded is))))
  
(extend-protocol ExtractionOps
  URL
  (parse [^URL url]
    (with-open [is (input-stream url)] (parse is)))
  (parse-extract-embedded [^URL url]
    (with-open [is (input-stream url)] (parse-extract-embedded is))))

(extend byte-array-type
  ExtractionOps
  {:parse (fn [^bytes input]
            (parse (ByteArrayInputStream. input)))
   :parse-extract-embedded (fn [^bytes input]
                             (parse-extract-embedded
                              (ByteArrayInputStream. input)))})
