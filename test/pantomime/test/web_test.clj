(ns pantomime.test.web-test
  (:use pantomime.web clojure.test)
  (:require [clojure.java.io :as io]
            [clj-http.client :as http]))



(deftest test-unified-http-response-content-type-detection
  (are [url expected-mime] (let [{:keys [^String body headers status]} (http/get url)]
                             (is (= 200 status))
                             (is (= expected-mime
                                    (mime-type-of (.getBytes body) headers)
                                    (mime-type-of body headers))))
       "http://files.travis-ci.org/docs/amqp/0.9.1/AMQP091Specification.pdf" "application/pdf"
       "http://github.com/robots.txt"                                        "text/plain"
       "http://www.amazon.com/sitemap-manual-index.xml"                      "application/xml"
       "http://docs.oracle.com/javase/7/docs/index.html"                     "application/xhtml+xml"
       "http://upload.wikimedia.org/wikipedia/commons/9/9a/PNG_transparency_demonstration_2.png" "image/png"
       "http://creativecommons.org/images/deed/cc-logo.jpg" "image/jpeg"
       "http://elpais.com.uy/formatos/rss/index.asp?seccion=umomento" "application/rss+xml"))
