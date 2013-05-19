(defproject enlive-demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [enlive "1.1.1"]]
  :ring {:handler enlive-demo.core/app}
  :plugins [[lein-ring "0.8.5"]])
