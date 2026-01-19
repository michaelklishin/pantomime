;; Copyright (c) 2011-2026 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.
(ns pantomime.test.extract-test
  (:require [clojure.java.io   :as io]
            [pantomime.extract :as extract]
            [clojure.test      :refer :all]))


(deftest test-extract-metadata
  (let [parsed (->  "resources/pdf/qrl.pdf"
                    io/resource
                    extract/parse)]
    (are [x y] (= (x parsed) (list y))
         :pdf/pdfversion  "1.2"
         :dc/title        "main.dvi")))

(deftest test-extract-metadata-input-stream
  (let [parsed (-> "resources/pdf/qrl.pdf"
                   io/resource
                   io/input-stream
                   extract/parse)]
    (are [x y] (= (x parsed) (list y))
         :pdf/pdfversion  "1.2"
         :dc/title        "main.dvi")))

(deftest test-extract-metadata-file
  (let [parsed (-> "resources/pdf/qrl.pdf"
                   io/resource
                   io/as-file
                   extract/parse)]
    (are [x y] (= (x parsed) (list y))
         :pdf/pdfversion  "1.2"
         :dc/title        "main.dvi")))

;; http://stackoverflow.com/questions/7181658/byte-collection-to-string-on-clojure
;; http://alexander-hill.tumblr.com/post/88883810180/working-with-binary-files-in-clojure
;; https://github.com/ztellman/byte-streams
(deftest test-extract-metadata-byte-array
  (let [file-path "test/resources/pdf/qrl.pdf"]
    (with-open [reader (io/input-stream (io/file file-path))]
      (let [length  (.length (clojure.java.io/file file-path))
            buffer  (byte-array length)
            _       (.read reader buffer 0 length)
            parsed  (extract/parse buffer)]
        (are [x y] (= (x parsed) (list y))
             :pdf/pdfversion  "1.2"
             :dc/title        "main.dvi")))))

(deftest test-extract-metadata-string
  (let [parsed (extract/parse "test/resources/txt/english.txt")]
    (are [x y] (= (x parsed) (list y))
         :content-encoding "ISO-8859-1")))

(deftest test-extract-text-contents
  (let [test-file    "resources/txt/english.txt"
        parsed       (extract/parse "test/resources/txt/english.txt")
        file-content (slurp (str "test/" test-file))]
    ;; org.apache.tika.parser.txt.TXTParser (I think) adds a newline
    ;; to parsed text :/
    (is (= (:text parsed) (str file-content "\n")))))

(deftest test-extract-embedded
  (let [parsed       (extract/parse "test/resources/pdf/fileAttachment.pdf")]
    (is (contains? parsed :text))
    (is (contains? parsed :dc/title))))

(deftest test-extract-URL
  (let [parsed (-> "https://pdfobject.com/pdf/sample.pdf"
                   io/as-url
                   extract/parse)]
    (is (contains? parsed :pdf/pdfversion))
    (is (contains? parsed :text))))

(deftest test-extract-URL-with-config
  (let [config (extract/make-config "test/resources/no-tesseract.xml")
        parsed (extract/parse (io/resource "resources/images/i_am_an_image.jpg") config)]
    (is (empty? (:text parsed)))))

(deftest test-make-config
  (let [config (extract/make-config "test/resources/no-tesseract.xml")]
    (is config)
    (is (= (type config) org.apache.tika.config.TikaConfig))))

(deftest test-extract-no-tesseract
  (let [config (extract/make-config "test/resources/no-tesseract.xml")
        parsed-without-tesseract (extract/parse "test/resources/images/i_am_an_image.jpg" config)]
    (is (empty? (:text parsed-without-tesseract)))))
