(ns pantomime.extract
  (:require [pantomime.internal :refer :all]
            [clojure.string :as string]
            [clojure.java.io :refer [input-stream copy]])
  (:import [java.io File InputStream ByteArrayInputStream]
           [org.apache.tika Tika]
           [java.net URL]
           [org.apache.tika.metadata Metadata]
           [org.apache.tika.sax BodyContentHandler]
           [org.apache.tika.parser Parser AbstractParser
            AutoDetectParser ParseContext]
           [org.apache.tika.config TikaConfig]))

(defn convert-key
  [k]
  (let [lisp-case (.toLowerCase ^String  (string/replace k \_ \-))
        segments (string/split lisp-case #":")
        nspace (butlast segments)
        n (last segments)]
    (if (seq nspace)
      (keyword (string/join "." nspace) n)
      (keyword n))))

(defn conv-metadata
  [^Metadata mdata]
  (let [names (.names mdata)]
    (zipmap (map convert-key names)
            (map #(seq (.getValues mdata ^String %1)) names))))

(def ^{:private true} autodetect-parser (AutoDetectParser.))

(defn extract-parser
  "Parser for extracting embedded documents"
  [embedded-meta]
  (proxy [AbstractParser] []
    (getSupportedTypes [context]
      (.getSupportedTypes ^Parser autodetect-parser context))
    (parse [stream handler metadata context]
      (let [tmp-fh           (File/createTempFile "pantomime-" "-embedded")
            meta             {:path (.getPath ^File tmp-fh)
                              :name (.get ^Metadata metadata "resourceName")}]
        (copy stream tmp-fh)
        (swap! embedded-meta conj meta)))))

(defn embed-parser
  "Parser for parsing embedded documents.  Rolls up name-value pairs into parent metadata."
  [parser ^Metadata meta]
  (proxy [AbstractParser] []
    (getSupportedTypes [context]
      (.getSupportedTypes ^Parser parser context))
    (parse [stream handler metadata context]
      (.parse parser stream handler metadata context)
      (doseq [^String name (.names metadata)
              ^String value (.getValues metadata name)]
        (.add meta name value)))))

(defprotocol ExtractionOps
  (parse
    [input]
    [input ^TikaConfig config] "Extract content and metadata, optionally with a Tika config.")
  (parse-extract-embedded
    [input]
    [input ^TikaConfig config] "Extract content and metadata,
  saving any embedded documents as temp files, returning paths to
  saved files in metadata.  Optionally provide a Tika config")
  (parse-embedded
    [input]
    [input ^TikaConfig config] "Extract content and metadata, rolling up any embedded metadata,
    optionally with a Tika config.")
  (make-config [input] "Make a Tika config object to subsequently tweak the parser"))

(defn do-parse
  [ifile ex config]
  (let [parser   (if config
                   (AutoDetectParser. ^TikaConfig config)
                   (AutoDetectParser.))
        embedded-meta (atom [])
        metadata (Metadata.)
        context-parser (case ex
                         :extract (extract-parser embedded-meta)
                         :parse (embed-parser parser metadata)
                         parser)
        context  (ParseContext.)
        handler  (BodyContentHandler. -1)]
    (.set context Parser context-parser)
    (.parse parser ifile handler metadata context)
    (if (= ex :extract)
      (assoc (conv-metadata metadata)
        :text     (.toString handler)
        :embedded @embedded-meta)
      (assoc (conv-metadata metadata)
        :text     (.toString handler)))))

(extend-protocol ExtractionOps
  InputStream
  (parse
    ([^InputStream ifile]
       (do-parse ifile :default nil))
    ([^InputStream ifile ^TikaConfig config]
       (do-parse ifile :default config)))
  (parse-extract-embedded
    ([^InputStream ifile]
       (do-parse ifile :extract nil))
    ([^InputStream ifile ^TikaConfig config]
       (do-parse ifile :extract config)))
  (parse-embedded
    ([^InputStream ifile]
       (do-parse ifile :parse nil))
    ([^InputStream ifile ^TikaConfig config]
       (do-parse ifile :parse config)))
  (make-config [^InputStream ifile]
    (TikaConfig. ifile)))
     
(extend-protocol ExtractionOps
  java.io.File
  (parse
    ([^File file]
       (with-open [is (input-stream file)] (parse is)))
    ([^File file ^TikaConfig config]
       (with-open [is (input-stream file)] (parse is config))))
  (parse-extract-embedded
    ([^File file]
       (with-open [is (input-stream file)] (parse-extract-embedded is)))
    ([^File file ^TikaConfig config]
       (with-open [is (input-stream file)] (parse-extract-embedded is config))))
  (parse-embedded
    ([^File file]
       (with-open [is (input-stream file)] (parse-embedded is)))
    ([^File file ^TikaConfig config]
       (with-open [is (input-stream file)] (parse-embedded is config))))
  (make-config [^File file] (TikaConfig. file)))
    
(extend-protocol ExtractionOps
  String
  (parse ([^String filename] (with-open [is (input-stream filename)] (parse is)))
         ([^String filename ^TikaConfig config] (with-open [is (input-stream filename)] (parse is config))))
  (parse-extract-embedded
      ([^String filename] (with-open [is (input-stream filename)] (parse-extract-embedded is)))
      ([^String filename ^TikaConfig config]
    (with-open [is (input-stream filename)] (parse-extract-embedded is config))))
  (parse-embedded
      ([^String filename] (with-open [is (input-stream filename)] (parse-embedded is)))
      ([^String filename ^TikaConfig config] (with-open [is (input-stream filename)] (parse-embedded is config))))
  (make-config [^String filename]
    (with-open [is (input-stream filename)] (make-config is))))
  
(extend-protocol ExtractionOps
  URL
  (parse
    ([^URL url]
       (with-open [is (input-stream url)] (parse is)))
    ([^URL url config]
       (with-open [is (input-stream url)] (parse is url))))
  (parse-extract-embedded
    ([^URL url]
       (with-open [is (input-stream url)] (parse-extract-embedded is)))
    ([^URL url ^TikaConfig config]
       (with-open [is (input-stream url)] (parse-extract-embedded is config))))
  (parse-embedded
    ([^URL url]
       (with-open [is (input-stream url)] (parse-embedded is)))
    ([^URL url ^TikaConfig config]
       (with-open [is (input-stream url)] (parse-embedded is config))))
  (make-config [^URL url]
    (with-open [is (input-stream url)] (make-config is))))

(extend byte-array-type
  ExtractionOps
  {:parse (fn [^bytes input]
            (parse (ByteArrayInputStream. input)))
   :parse-extract-embedded (fn [^bytes input]
                             (parse-extract-embedded
                              (ByteArrayInputStream. input)))})

