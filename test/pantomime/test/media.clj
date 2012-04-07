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
  (is (= media/TEXT_HTML         (media/base-type "text/html; charset=UTF-8")))
  (is (= media/TEXT_HTML         (media/base-type (MediaType/parse "text/html; charset=UTF-8"))))
  (is (= media/APPLICATION_XHTML (media/base-type "application/xhtml+xml; charset=UTF-8"))))


(deftest test-charset
  (is (= "iso-8859-1" (media/charset-of "text/html; charset=iso-8859-1")))
  (is (= "UTF-8"      (media/charset-of (MediaType/parse "text/html; charset=UTF-8"))))
  (is (= "UTF-8"      (media/charset-of "application/xhtml+xml; charset=UTF-8")))
  (is (nil? (media/charset-of "text/html")))
  (is (nil? (media/charset-of "text/html; key=val"))))


(deftest test-multipart-predicate
  (are [mt] (is (media/multipart? mt))
       "multipart/form-data"
       "multipart/mixed"
       "multipart/*"
       "multipart/mixed; boundary=gc0p4Jq0M2Yt08j34c0p"
       "MULTIPART/mixed; boundary=gc0p4Jq0M2Yt08j34c0p"
       (media/media-type-named "multipart/mixed; boundary=gc0p4Jq0M2Yt08j34c0p")))

(deftest test-application-predicate
  (are [mt] (is (media/application? mt))
       "application/postscript"
       "application/octet-stream"
       "application/vnd.ms-excel"
       "application/vnd.oasis.opendocument.text"
       "application/vnd.oasis.opendocument.spreadsheet"
       "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
       "application/msword"
       "application/atom+xml"
       "application/ecmascript"
       "application/EDI-X12"
       "application/javascript"
       "application/pdf"
       "application/rdf+xml"
       "application/rss+xml"
       "application/atom+xml"
       "application/xhtml+xml"
       "application/x-gzip"
       "application/zip"
       "application/font-woff"
       "application/x-font-woff"
       "application/x-tar"
       "application/x-deb"
       (media/media-type-named "application/json")
       (media/media-type-named "application/vnd.custom; vendor=MegaCorp")))

(deftest test-image-predicate
  (are [mt] (is (media/image? mt))
       "image/png"
       "image/jpeg"
       (media/media-type-named "image/gif")
       (media/media-type-named "image/jpeg; vendor=MegaCorp")))

(deftest test-audio-predicate
  (are [mt] (is (media/audio? mt))
       "audio/basic"
       "audio/vnd.wave"
       "audio/webm"
       "audio/x-ms-wma"
       "audio/x-pn-realaudio-plugin"
       (media/media-type-named "audio/mp3")))

(deftest test-video-predicate
  (are [mt] (is (media/video? mt))
       "video/mpeg"
       "video/mp4"
       "video/quicktime"
       "video/webm"
       "video/x-ms-wmv"
       (media/media-type-named "video/quicktime")))
