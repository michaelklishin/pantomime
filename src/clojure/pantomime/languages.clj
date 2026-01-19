;; Copyright (c) 2011-2026 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns pantomime.languages
  "Natural language detection functions.

   Note that not all languages are currently supported by Tika,
   and, in turn, Pantomime."
  (:require [clojure.java.io :as io])
  (:import [org.apache.tika.langdetect.optimaize OptimaizeLangDetector]
           [org.apache.tika.language.detect LanguageDetector]
           java.net.URL
           java.io.File
           java.io.InputStream))


;;
;; API
;;

(defn- create-detector
  "Creates and initializes an OptimaizeLangDetector with loaded models."
  ^LanguageDetector []
  (-> (OptimaizeLangDetector.)
      (.loadModels)))

(defprotocol LanguageDetection
  (detect-language [arg] "Identifies natural language of the input"))

(extend-protocol LanguageDetection
  String
  (detect-language [^String source]
    (-> (create-detector)
        (.detect source)
        (.getLanguage)))

  File
  (detect-language [^File source]
    (detect-language (slurp source)))

  InputStream
  (detect-language [^InputStream source]
    (detect-language (slurp source)))

  URL
  (detect-language [^URL source]
    (detect-language (slurp source))))


(def supported-languages
  "A set of languages supported by the Tika language detector,
   and, in turn, Pantomime. Uses ISO 639-1 language codes."
  #{"af" "an" "ar" "ast" "be" "bg" "bn" "br" "ca" "cs" "cy" "da" "de"
    "el" "en" "es" "et" "eu" "fa" "fi" "fr" "ga" "gl" "gu" "he" "hi"
    "hr" "ht" "hu" "id" "is" "it" "ja" "km" "kn" "ko" "lt" "lv" "mk"
    "ml" "mr" "ms" "mt" "ne" "nl" "no" "oc" "pa" "pl" "pt" "ro" "ru"
    "sk" "sl" "so" "sq" "sr" "sv" "sw" "ta" "te" "th" "tl" "tr" "uk"
    "ur" "vi" "wa" "yi" "zh-cn" "zh-tw"})
