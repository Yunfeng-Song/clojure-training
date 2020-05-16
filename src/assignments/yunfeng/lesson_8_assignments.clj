(ns assignments.yunfeng.lesson-8-assignments
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]]))

(def bing-url "https://www.bing.com/search?q=")
(def yahoo-url "https://au.search.yahoo.com/search?p=")
(def yippy-url "https://www.yippy.com/search?query=")


(defn search
  [search-string search-url c]
  (let [result (slurp (str search-url search-string))]
    (go (>! c result))))


;;Compared with future and deliver approach, although only one result is printed it is possible to get all the other results(c1 c2 c3).
(defn search-3
  [search-string]
  (let [c1 (chan) c2 (chan) c3 (chan)]
    (search search-string bing-url c1)
    (search search-string yahoo-url c2)
    (search search-string yippy-url c3)
    (let [[result channel] (alts!! [c1 c2 c3])]
      (condp = channel
        c1  (println "Bing is Winning." result)
        c2  (println "Yahoo is Winning." result)
        c3  (println "Yippy is Winning." result)))))

(search-3 "ahkjsghjksgjk")


