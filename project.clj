(defproject com.novemberain/pantomime "2.12.0-SNAPSHOT"
  :min-lein-version "2.5.1"
  :description "A minimalistic Clojure interface to Apache Tika"
  :url "http://github.com/michaelklishin/pantomime"
  :license { :name "Eclipse Public License" }
  :source-paths ["src/clojure"]
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.apache.tika/tika-parsers "1.19.1"]
                 [org.apache.tika/tika-dl "1.19.1"]
                 [org.apache.commons/commons-compress "1.18"]]
  :profiles {:dev {:resource-paths ["test/resources"]
                   :dependencies [[clj-http "3.9.1"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0"]]}
             :master {:dependencies [[org.clojure/clojure "1.11.0-master-SNAPSHOT"]]}
             }
  :repositories {"sonatype" {:url "https://oss.sonatype.org/content/repositories/releases"
                             :snapshots false
                             :releases {:checksum :fail :update :always}}
                 "sonatype-snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots"
                                       :snapshots true
                                       :releases {:checksum :fail :update :always}}}
  :aliases  {"all" ["with-profile" "+dev:+1.8:+master"]}
  :global-vars {*warn-on-reflection* true})
