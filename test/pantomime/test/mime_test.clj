(ns pantomime.test.mime-test
  (:require [clojure.java.io :as io]
            [clj-http.client :as http]
            [pantomime.mime  :refer :all]
            [clojure.test    :refer :all])
  (:import [java.io File FileInputStream]
           java.net.URL))


(deftest test-content-type-detection
  (let [tmp-file (File/createTempFile "pantomime.test.core" "test-content-type-detection-with-java-io-file-instances")
        _        (spit tmp-file "Some content")]
    (are [a b] (is (= a (mime-type-of b)))
         "image/png"                "an_awesome_icon.png"
         "application/pdf"          "tika_in_action.pdf"
         "application/pdf"          (io/resource "pdf/qrl.pdf")
         "application/pdf"          (io/input-stream (io/resource "pdf/qrl.pdf"))
         "application/pdf"          (io/as-file (io/resource "pdf/qrl.pdf"))
         ;; slurp turns content into a string
         "application/octet-stream" (slurp (io/resource "pdf/qrl.pdf"))
         "application/epub+zip"     "tika_in_action.epub"
         "application/xhtml+xml"    (io/input-stream (io/resource "html/wired_com_cars1"))
         "application/xhtml+xml"    (io/input-stream (io/resource "html/wired_com_reviews1"))
         "application/xhtml+xml"    (.getBytes (slurp (io/resource "html/wired_com_reviews1")))
         "application/xhtml+xml"    (io/input-stream (io/resource "html/arstechnica.com_full"))
         "application/xhtml+xml"    (io/input-stream (io/resource "html/page_in_german"))
         "text/plain"               (io/resource "txt/a_text_file1.txt")
         "text/plain"               (io/resource "txt/a_text_file2")
         "image/svg+xml"            (io/resource "images/svg-logo.svg")
         "text/plain"               tmp-file
         "text/plain"               (FileInputStream. tmp-file)
         "image/jpeg"               (URL. "http://www.google.com/logos/2012/newyearsday-2012-hp.jpg")
         "image/gif"                (io/resource "images/feather-small.gif")
         "image/png"                (io/resource "images/clojure.glyph.png")
         "image/png"                (io/resource "images/clojure.glyph.png")
         "application/octet-stream" (.getBytes (slurp (io/resource "images/clojure.glyph.png")))
         "image/png"                (io/resource "images/png.image.unknown")
         "application/octet-stream" "an_awesome_icon"

         "application/javascript"   (io/resource "application/javascript/mootools.uncompressed.js")
         "text/plain"               (io/input-stream (io/resource "application/javascript/mootools.uncompressed.js"))
         "application/javascript"   (io/as-file (io/resource "application/javascript/mootools.uncompressed.js"))

         "application/javascript"   (io/resource "application/javascript/mootools.compressed.js")
         "text/plain"               (io/input-stream (io/resource "application/javascript/mootools.compressed.js"))
         "application/javascript"   (io/as-file (io/resource "application/javascript/mootools.compressed.js")))))

(deftest test-http-response-content-type-detection
  (are [url expected-mime] (let [{:keys [^String body headers status]} (http/get url)]
                             (is (= 200 status))
                             (is (= expected-mime (mime-type-of (.getBytes body)))))
       ;;Doesn't respond
       ;;"https://www.rabbitmq.com/resources/specs/amqp0-9-1.pdf"              "application/pdf"
       "http://github.com/robots.txt"                                        "text/plain"
       "https://www.xml-sitemaps.com/sitemap.xml"                            "application/xml"
       "http://docs.oracle.com/javase/7/docs/index.html"                     "application/xhtml+xml"
       "http://upload.wikimedia.org/wikipedia/commons/9/9a/PNG_transparency_demonstration_2.png" "application/octet-stream"
       "http://upload.wikimedia.org/wikipedia/commons/e/e9/Felis_silvestris_silvestris_small_gradual_decrease_of_quality.png" "application/octet-stream"))

(deftest test-extension-for-name
  (are [name ext] (is (= ext (extension-for-name name)))
    "application/json"         ".json"
    "application/vnd.ms-excel" ".xls"
    "image/jpeg"               ".jpg"
    "application/xhtml+xml"    ".xhtml"
    "application/octet-stream" ".bin"
    "g-d/knows/what"           ""))

(deftest test-adding-pattern
  (is (= (mime-type-of "lorem.ipsum") "application/octet-stream")) ;; fallback mime type
  (add-pattern "text/lorem-ipsum" ".+\\.ipsum$" "lorem.ipsum")
  (is (= (mime-type-of "lorem.ipsum") "text/lorem-ipsum")))
