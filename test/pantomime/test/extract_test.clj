(ns pantomime.test.extract-test
  (:require [clojure.java.io   :as io]
            [pantomime.extract :as extract]
            [clojure.test      :refer :all])
  (:import [java.io File FileInputStream]
           java.net.URL))


(deftest test-extract-pdf-metadata
  (let [parsed (extract/parse (io/resource "resources/pdf/qrl.pdf"))]
    (are [x y] (= (x parsed) (list y))
         :pdf:pdfversion  "1.2"
         :dc:title        "main.dvi")))

(deftest test-extract-pdf-metadata-stream
  (let [parsed (extract/parse (io/input-stream (io/resource "resources/pdf/qrl.pdf")))]
    (are [x y] (= (x parsed) (list y))
         :pdf:pdfversion  "1.2"
         :dc:title        "main.dvi")))

(deftest test-extract-pdf-metadata-file
  (let [parsed (extract/parse (io/as-file (io/resource "resources/pdf/qrl.pdf")))]
    (are [x y] (= (x parsed) (list y))
         :pdf:pdfversion  "1.2"
         :dc:title        "main.dvi")))

;; failing.
;; http://stackoverflow.com/questions/7181658/byte-collection-to-string-on-clojure
;; http://alexander-hill.tumblr.com/post/88883810180/working-with-binary-files-in-clojure
;; https://github.com/ztellman/byte-streams
(deftest test-extract-pdf-metadata-string
  (let [file-path "test/resources/pdf/qrl.pdf"]
    (with-open [reader (io/input-stream file-path)]
      (let [length  (.length (clojure.java.io/file file-path))
            buffer  (byte-array length)
            _       (.read reader buffer 0 length)
            content (apply str (map #(char (bit-and % 255)) buffer))
            parsed  (extract/parse content)]
        (are [x y] (= (x parsed) (list y))
             :pdf:pdfversion  "1.2"
             :dc:title        "main.dvi")))))
            
(deftest test-extract-text-contents
  (let [test-file    "resources/txt/english.txt"
        parsed       (extract/parse (io/resource test-file))
        ;; frustratingly, tika.sax.BodyContentHandler or
        ;; tika.parser.txt.TXTParser seems to add a newline to the end
        ;; of the file contents.
        file-content (str (slurp (str "test/" test-file)) "\n")]
    (is (= (:text parsed) file-content))))

(deftest test-extract-jpg
  (let [parsed (extract/parse (io/resource "resources/images/feather-small.gif"))]
    (are [x y] (= (x parsed) y)
         :text   ""
         (keyword "compression lossless") (list "true"))))
