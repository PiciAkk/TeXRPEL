(ns app.core
  (:require [clojure.string :as s]
            [app.latex :refer [show-latex]]
            [app.wolframalpha :refer [evaluate-latex]]
            [app.config :refer [config]]) 
  (:gen-class :main true))

(defn -main []
  (println "Welcome to TeXRPEL.")
  (println "You can exit by entering `exit`.")
  (println "You can clear the console by entering `clear`.")
  (while true
    (let [expression (do (print "latex> ")
                         (flush)
                         (read-line))]

      (when-not (s/blank? expression)
        (case expression
          "clear" (println "\033[H\033[2J")
          "exit" (System/exit 0)
          (do (when (get-in config ["latex" "enabled"])
                (show-latex expression))

              (when (get-in config ["wolframalpha" "enabled"])
                (println (evaluate-latex expression)))))))))