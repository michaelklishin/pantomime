(ns pantomime.test.core
  (:use [pantomime.core])
  (:use [clojure.test]))


(deftest test-extension-extraction
  (is (= "png"  (extension-of "pic.png")))
  (is (= "6"    (extension-of "obj.6")))
  (is (= "gif"  (extension-of "logo.gif")))
  (is (= "txt"  (extension-of "memo.txt")))
  (is (= "avi"  (extension-of "some.dot.separated.path.avi")))
  (is (= "tiff" (extension-of "a_tiff-image.tiff")))
  (is (= "app"  (extension-of "Aperçu.app")))
  (is (= "mov"  (extension-of "A\\ nice\\  movie.clip.mov"))))

(deftest test-content-type-detection-for-extension
  (is (= "image/png"       (mime-type-for "png")))
  (is (= "application/zip" (mime-type-for "ZIP")))
  (is (= "video/quicktime" (mime-type-for "moV")))
  (is (= "image/jpeg"      (mime-type-for "jpe")))
  (is (= "image/jpeg"      (mime-type-for "jpg")))
  (is (= "image/jpeg"      (mime-type-for "jpeg")))
  (is (nil?                (mime-type-for ""))))

(deftest test-content-type-detection
  (is (= "image/png" (mime-type-of "an_awesome_icon.png")))
  (is nil?           (mime-type-of "an_awesome_icon")))

;; (deftest test-content-type-detection-for-png-with-overriden-types-map
;;  (is (= "image/awesomesauce" (mime-type-for "an_awesome_icon.png" { "png" "image/awesomesauce" })) "Content type of .png is image/png"))
