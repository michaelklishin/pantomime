(ns pantomime.test.media
  (:use [clojure.test])
  (:require [pantomime.media :as media]))


(deftest test-media-type-named
  (is (= media/TEXT_PLAIN       (media/media-type-named "text/plain")))
  (is (= media/TEXT_HTML        (media/media-type-named "text/html")))
  (is (= media/APPLICATION_XML  (media/media-type-named "application/xml")))
  (is (= media/APPLICATION_JSON (media/media-type-named "application/json"))))
