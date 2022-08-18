(defproject psychic-pancake "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[ch.qos.logback/logback-classic "1.2.10"]
                 [clojure.java-time "0.3.3"]
                 [cprop "0.1.19"]
                 [expound "0.9.0"]
                 [funcool/struct "1.4.0"]
                 [json-html "0.4.7"]
                 [luminus-http-kit "0.1.9"]
                 [luminus-transit "0.1.5"]
                 [luminus/ring-ttl-session "0.3.3"]
                 [markdown-clj "1.10.8"]
                 [metosin/muuntaja "0.6.8"]
                 [metosin/reitit "0.5.15"]
                 [metosin/ring-http-response "0.9.3"]
                 [mount "0.1.16"]
                 [nrepl "0.9.0"]
                 [org.clojure/clojure "1.11.1"]
                 [org.clojure/tools.cli "1.0.206"]
                 [org.clojure/tools.logging "1.2.4"]
                 [org.webjars.npm/bulma "0.9.3"]
                 [org.webjars.npm/material-icons "1.0.0"]
                 [org.webjars/webjars-locator "0.42"]
                 [ring-webjars "0.2.0"]
                 [ring-cors "0.1.13"]
                 [ring/ring-core "1.9.5"]
                 [ring/ring-defaults "0.3.3"]
                 [selmer "1.12.50"]
                 [org.clojure/data.csv "1.0.1"]
                 ;; jpa
                 [org.hibernate/hibernate-core "6.1.1.Final"]
                 [org.postgresql/postgresql "42.4.0"]
                 [org.jboss.weld.se/weld-se-core "5.0.1.Final"]
                 [javax.persistence/javax.persistence-api "2.2"]
                 [org.projectlombok/lombok "1.18.24"]
                 [jakarta.validation/jakarta.validation-api "3.0.2"]
                 ;; buddy
                 [buddy/buddy-auth "3.0.1"]
                 [buddy/buddy-sign "3.4.333"]
                 [buddy/buddy-hashers "1.8.158"]]

  :min-lein-version "2.0.0"
  
  :source-paths ["src/clj" "env/dev/clj"]
  :java-source-paths ["src/java"]
  :test-paths ["test/clj"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :compile-path "%s/classes"
  :main ^:skip-aot psychic-pancake.core

  :plugins [[lein-kibit "0.1.2"]] 

  :profiles
  {:uberjar {:omit-source true
             :aot :all
             :uberjar-name "psychic-pancake.jar"
             :source-paths ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:jvm-opts ["-Dconf=dev-config.edn" ]
                  :dependencies [[org.clojure/tools.namespace "1.2.0"]
                                 [pjstadig/humane-test-output "0.11.0"]
                                 [prone "2021-04-23"]
                                 [ring/ring-devel "1.9.5"]
                                 [ring/ring-mock "0.4.0"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.24.1"]
                                 [jonase/eastwood "0.3.5"]
                                 [cider/cider-nrepl "0.26.0"]] 
                  :source-paths ["env/dev/clj" ]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns psychic-pancake.user
                                 :timeout 120000
                                 :port 7000
                                 :host "0.0.0.0"
                                 :init (start)}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:jvm-opts ["-Dconf=test-config.edn" ]
                  :resource-paths ["env/test/resources"] }
   :profiles/dev {}
   :profiles/test {}})
