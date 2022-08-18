(ns app.core-test
  (:require [clojure.test :refer :all]
            [app.core :refer :all]
            [app.wolframalpha :refer [evaluate-latex]]))

(deftest wolframalpha-basic-test
  (testing "wolframalpha api is working"
    (is (= (evaluate-latex "pi") "π"))))
