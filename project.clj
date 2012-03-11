(defproject com.novemberain/pantomime "1.2.0-SNAPSHOT"
  :min-lein-version "2.0.0"
  :description "A tiny Clojure library that deals with MIME types"  
  :license { :name "Eclipse Public License" }
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.apache.tika/tika-core "1.0"]]
  :profiles {:all { :dependencies [[org.apache.tika/tika-core "1.0"]] }
             :dev { :resource-paths ["test/resources"]}
             :1.4 { :resource-paths ["test/resources"]
                    :dependencies [[org.clojure/clojure "1.4.0-beta4"]] }}
  :warn-on-reflection true)
