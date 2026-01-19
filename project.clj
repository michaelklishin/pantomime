(defproject com.novemberain/pantomime "3.2.0-SNAPSHOT"
  :min-lein-version "2.5.1"
  :description "A minimalistic Clojure interface to Apache Tika"
  :url "http://github.com/michaelklishin/pantomime"
  :license { :name "Eclipse Public License" }
  :source-paths ["src/clojure"]
  :dependencies [[org.clojure/clojure "1.12.4"]
                 [org.apache.tika/tika-core "3.2.3"]
                 [org.apache.tika/tika-parsers-standard-package "3.2.3"]
                 [org.apache.tika/tika-langdetect-optimaize "3.2.3"]]
  :managed-dependencies [[org.bouncycastle/bcpkix-jdk18on "1.81"]
                         [org.bouncycastle/bcutil-jdk18on "1.81"]
                         [org.bouncycastle/bcprov-jdk18on "1.81"]]
  :profiles {:dev {:resource-paths ["test/resources"]
                   :dependencies [[clj-http "3.13.1"]]}
             :1.10 {:dependencies [[org.clojure/clojure "1.10.3"]]}
             :1.11 {:dependencies [[org.clojure/clojure "1.11.4"]]}
             :1.12 {:dependencies [[org.clojure/clojure "1.12.4"]]}
             }
  :repositories {"sonatype" {:url "https://oss.sonatype.org/content/repositories/releases"
                             :snapshots false
                             :releases {:checksum :fail :update :always}}
                 "sonatype-snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots"
                                       :snapshots true
                                       :releases {:checksum :fail :update :always}}}
  :deploy-repositories {"releases" {:url "https://repo.clojars.org"
                                    :username :env/clojars_username
                                    :password :env/clojars_password}}
  :aliases  {"all" ["with-profile" "+dev:+1.10:+1.11:+1.12"]}
  :global-vars {*warn-on-reflection* true})
