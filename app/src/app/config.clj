(ns app.config
  (:require [clojure.data.json :as json]
            [clojure.java.io :refer [file]]))

(def config-file
  (str (System/getProperty "user.home")
       "/.config/texrpel.json"))

(when-not (.exists (file config-file)) 
  (spit config-file 
        (json/write-str
         {"latex" {"enabled" true}
          "wolframalpha" {"enabled" false
                          "appid" "FIXTHIS"}}))
  
  (println "config file successfully created! set your appid in ~/.config/texrpel.json!"))

(def config
  (json/read-str (slurp config-file)))