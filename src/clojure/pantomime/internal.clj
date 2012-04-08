(ns pantomime.internal)

;; clojure.java.io has these as private, so we had to copy them. MK.
(def ^{:doc "Type object for a Java primitive byte array."}
  byte-array-type (class (make-array Byte/TYPE 0)))

(def ^{:doc "Type object for a Java primitive char array."}
  char-array-type (class (make-array Character/TYPE 0)))
