;; Copyright (c) 2011-2026 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.
(ns pantomime.test.web-test
  (:require [clj-http.client :as http]
            [clojure.test  :refer :all]
            [pantomime.web :refer :all]))



(deftest test-unified-http-response-content-type-detection
  (are [url expected-mime] (let [{:keys [^String body headers status]} (http/get url)]
                             (is (= 200 status))
                             (is (= expected-mime
                                    (mime-type-of (.getBytes body) headers)
                                    (mime-type-of body headers))))
       "http://github.com/robots.txt"                          "text/x-robots"
       "https://www.xml-sitemaps.com/sitemap.xml"              "application/xml"
       "https://docs.oracle.com/en/java/javase/21/"            "text/html"
       "http://rss.cnn.com/rss/edition_travel.rss"             "application/rss+xml"))
