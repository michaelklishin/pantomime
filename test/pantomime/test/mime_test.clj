(ns pantomime.test.mime-test
  (:use pantomime.mime clojure.test)
  (:require [clojure.java.io :as io]
            [clj-http.client :as http])
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
         "text/plain"               tmp-file
         "text/plain"               (FileInputStream. tmp-file)
         "image/jpeg"               (URL. "http://www.google.com/logos/2012/newyearsday-2012-hp.jpg")
         "image/gif"                (io/resource "images/feather-small.gif")
         "application/octet-stream" "an_awesome_icon")))

(deftest test-http-response-content-type-detection
  (testing "detection for PDF documents"
    (let [{:keys [^String body headers status]} (http/get "http://files.travis-ci.org/docs/amqp/0.9.1/AMQP091Specification.pdf")]
    (is (= 200 status))
    (is (= "application/pdf" (mime-type-of (.getBytes body))))))
  (testing "detection for text documents"
    (let [{:keys [^String body headers status]} (http/get "http://github.com/robots.txt")]
    (is (= 200 status))
    (is (= "text/plain" (mime-type-of (.getBytes body))))))
  (testing "detection for XML documents"
    (let [{:keys [^String body headers status]} (http/get "http://www.amazon.com/sitemap-manual-index.xml")]
    (is (= 200 status))
    (is (= "application/xml" (mime-type-of (.getBytes body)))))))
