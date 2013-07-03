(ns pantomime.languages
  "Natural language detection functions.

   Note that not all languages are currently supported by Tika,
   and, in turn, Pantomime."
  (:require [clojure.java.io :as io])
  (:import org.apache.tika.language.LanguageIdentifier
           java.net.URL
           java.io.File
           java.io.InputStream))


;;
;; API
;;

(defprotocol LanguageDetection
  (detect-language [arg] "Identifies natural language of the input"))

(extend-protocol LanguageDetection
  String
  (detect-language [^String source]
    (.getLanguage (LanguageIdentifier. source)))

  File
  (detect-language [^File source]
    (detect-language (slurp source)))

  InputStream
  (detect-language [^InputStream source]
    (detect-language (slurp (io/as-file source))))

  URL
  (detect-language [^URL source]
    (detect-language (slurp source))))


(def supported-languages
  "A set of languages supported by the Tika language detector,
   and, in turn, Pantomime"
  (set (sort (LanguageIdentifier/getSupportedLanguages))))
