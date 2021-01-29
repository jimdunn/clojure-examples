(defproject latex-swing "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.apache.xmlgraphics/batik-swing "1.14"]
                 [org.apache.xmlgraphics/batik-svggen "1.14"]
                 [org.scilab.forge/jlatexmath "1.0.7"]]
  :repl-options {:init-ns latex-swing.core})
