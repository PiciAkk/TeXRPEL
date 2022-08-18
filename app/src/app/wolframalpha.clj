(ns app.wolframalpha
  (:require [clj-http.client :as client] 
            [clojure.xml :as xml]
            [app.config :refer [config]]))

(defn query [appid input]
  (let [url (str "http://api.wolframalpha.com/v2/query?input="
                 input
                 "&appid="
                 appid)]
    
    (->> (client/get url)
         (:body)
         (.getBytes)
         (java.io.ByteArrayInputStream.)
         (xml/parse))))

(defn evaluate-latex [input]
  (let [content (->> (query (get-in config 
                                    ["wolframalpha" "appid"])
                            input)
                     (:content))

        [get-result get-exact-result get-solution]
        (map (fn [result-type]
               (comp :alt
                     :attrs
                     first
                     :content
                     first
                     :content
                     first
                     (partial filter (comp (partial = result-type)
                                           :title
                                           :attrs))))
             ["Result"
              "Exact result"
              "Solution"])

        get-alt (comp :alt
                      :attrs
                      first
                      :content
                     ;  first
                      :content
                      first)]

    (->> [(get-result content)
          (get-exact-result content)
          (get-solution content)
          (get-alt content)]
         (filter some?)
         (first))))

(comment (evaluate-latex (read-line)))