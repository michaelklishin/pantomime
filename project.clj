(defproject com.novemberain/pantomime "1.4.0"
  :min-lein-version "2.0.0"
  :description "A tiny Clojure library that deals with MIME types"
  :license { :name "Eclipse Public License" }
  :source-paths ["src/clojure"]
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.apache.tika/tika-core "1.2"]]
  :profiles {:dev {:resource-paths ["test/resources"]
                   :dependencies [[clj-http "0.4.4"]]}
             :1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.0-master-SNAPSHOT"]]}}
  :repositories {"clojure-releases" "http://build.clojure.org/releases"
                 "sonatype" {:url "http://oss.sonatype.org/content/repositories/releases"
                             :snapshots false
                             :releases {:checksum :fail :update :always}}}
  :aliases  {"all" ["with-profile" "dev:dev,1.4:dev,1.5"]
             "ci"  ["with-profile" "dev:dev,1.4"]}
  :warn-on-reflection true)
