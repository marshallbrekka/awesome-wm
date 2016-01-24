(defproject awesome-wm "0.0.1"
  :description "Start of tiling window manager. Just a slate replacement for now."
  :url "https://github.com/marshallbrekka/awesome-wm"
  :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                 [org.clojure/clojurescript "0.0-3308"]
                 [weasel "0.7.0"]]
  :source-paths ["src"]
  :profiles {:dev {:dependencies [[com.cemerick/piggieback "0.2.1"]
                                  [org.clojure/tools.nrepl "0.2.10"]]
                   :plugins [[lein-cljsbuild "1.0.6"]]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :cljsbuild {:builds [{:id "awesome-wm"
                                         :source-paths ["src"]
                                         :compiler {:output-to "awesome-wm.js"
                                                    :output-dir "out"
                                                    :optimizations :whitespace}}]}}})
