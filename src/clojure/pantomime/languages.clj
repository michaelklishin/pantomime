;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
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
