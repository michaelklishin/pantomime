;; Copyright (c) 2011-2026 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.
(ns pantomime.test.languages-test
  (:require [clojure.test :refer :all]
            [pantomime.languages :as pl]
            [clojure.java.io :as io]))


(deftest test-language-detection-with-strings
  (are [source lng] (is (= (pl/detect-language (slurp (io/resource source))) lng))
       ;; unfortunately, many languages are not supported
       ;; by Tika yet. MK.
       "txt/english.txt"    "en"
       "txt/russian.txt"    "ru"
       "txt/portuguese.txt" "pt"
       "txt/spanish.txt"    "es"
       "txt/dutch.txt"      "nl"))

(deftest test-language-detection-with-urls
  (are [source lng] (is (= (pl/detect-language (io/resource source)) lng))
       "txt/english.txt"    "en"
       "txt/russian.txt"    "ru"
       "txt/portuguese.txt" "pt"
       "txt/spanish.txt"    "es"
       "txt/dutch.txt"      "nl"))

(deftest test-language-detection-with-files
  (are [source lng] (is (= (pl/detect-language (io/as-file (io/resource source))) lng))
       "txt/english.txt"    "en"
       "txt/russian.txt"    "ru"
       "txt/portuguese.txt" "pt"
       "txt/spanish.txt"    "es"
       "txt/dutch.txt"      "nl"))

(deftest test-supported-languages
  (is (set? pl/supported-languages)))
