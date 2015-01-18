(ns pantomime.test.web-test
  (:require [clojure.java.io :as io]
            [clj-http.client :as http]
            [clojure.test  :refer :all]
            [pantomime.web :refer :all]))



(deftest test-unified-http-response-content-type-detection
  (are [url expected-mime] (let [{:keys [^String body headers status]} (http/get url)]
                             (is (= 200 status))
                             (is (= expected-mime
                                    (mime-type-of (.getBytes body) headers)
                                    (mime-type-of body headers))))
       "http://www.rabbitmq.com/resources/specs/amqp0-9-1.pdf" "application/pdf"
       "http://github.com/robots.txt"                          "text/plain"
       "https://www.xml-sitemaps.com/sitemap.xml"              "application/xml"
       "http://docs.oracle.com/javase/7/docs/index.html"       "application/xhtml+xml"
       "http://upload.wikimedia.org/wikipedia/commons/9/9a/PNG_transparency_demonstration_2.png" "image/png"
       "http://creativecommons.org/images/deed/cc-logo.jpg" "image/jpeg"
       "http://elpais.com.uy/formatos/rss/index.asp?seccion=umomento" "application/rss+xml"))
