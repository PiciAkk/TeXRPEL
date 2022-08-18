(ns app.latex
  (:require [clojure.java.shell :refer [sh]]
            [clj-latex.core :refer [render-latex]]
            [clojure.string :as s]
            [clojure.java.io :refer [delete-file]]))

(defn change-file-extension [filename to]
  (let [without-file-extension
        (->> (s/last-index-of filename ".")
             (subs filename 0))]

    (str without-file-extension "." to)))

(defn show-latex [expression]
  (let [latex-file (->> (sh "mktemp"
                            "/tmp/latexXXX.tex")
                        (:out)
                        (s/trim))

        [png-file
         dvi-file
         log-file
         aux-file] (map (partial change-file-extension
                                 latex-file)
                        ["png" "dvi" "log" "aux"])]

    (spit latex-file
          (render-latex
           (:documentclass [12] "article")
           (:pagestyle "empty")
           ('document
            (str "$ \\displaystyle " expression "$"))))

    (sh "latex"
        "-output-directory=/tmp/"
        "-aux-directory=/tmp"
        latex-file)

    (sh "dvipng"
        "-o"
        png-file
        "-T"
        "tight"
        dvi-file)

    (sh "bash"
        "-c"
        (str "feh " png-file " & sleep 0.1 && i3-msg floating enable"))

    (run! delete-file [latex-file
                       png-file
                       dvi-file
                       log-file
                       aux-file])))