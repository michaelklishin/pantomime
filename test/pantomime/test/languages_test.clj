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
