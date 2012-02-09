(ns pantomime.test.media
  (:use [clojure.test])
  (:require [pantomime.media :as media])
  (:import [org.apache.tika.mime MediaType]))


(deftest test-media-type-named
  (is (= media/TEXT_PLAIN       (media/media-type-named "text/plain")))
  (is (= media/TEXT_HTML        (media/media-type-named "text/html")))
  (is (= media/TEXT_HTML        (media/media-type-named "text/HTML")))
  (is (= media/APPLICATION_XML  (media/media-type-named "application/xml")))
  (is (= media/APPLICATION_JSON (media/media-type-named "application/json"))))

(deftest test-base-media-types
  (is (= media/TEXT_HTML (media/base-type  "text/html; charset=UTF-8")))
  (is (= media/TEXT_HTML (media/base-type (MediaType/parse "text/html; charset=UTF-8")))))

