(ns examples.exploring
  (:require [clojure.string :as str])
  (:import (java.io File)))

(defn greeting
  "Returns a greeting of the form 'Hello, username'
   Default username is 'world'"
  ([] (greeting "world"))
  ([username] (str "Hello, " username)))

(defn date [person-1 person-2 & chaperones]
  (println person-1 "and" person-2
           "went out with" (count chaperones) "chaperones."))

(defn indexable-word? [word]
  (> (count word) 2))
;; ヘルパー関数で
;(filter indexable-word? (str/split "A fine day it is" #"\W+"))
;; 無名関数(fn)で
;(filter (fn [w] (> (count w) 2)) (str/split "A fine day it is" #"\W+"))
;; さらに短く
;(filter #(> (count %) 2) (str/split "A fine day it is" #"\W+"))

(defn indexable-words [text]
  (let [indexable-word? (fn [w] (> (count w) 2))]
    (filter indexable-word? (str/split text #"\W+"))))

(defn make-greeter [greeting-prefix]
  (fn [username] (str greeting-prefix ", " username)))

;(def hello-greeting (make-greeter "Hello"))
;(hello-greeting "Foo")
;; 名前をつけずに関数を作ってすぐ呼び出せる
;((make-greeter "Howdy") "pardner")

(defn square-corners [bottom left size]
  (let [top (+ bottom size)
        right (+ left size)]
    [[bottom left] [top left] [top right] [bottom right]]))

(defn greet-author-1 [author]
  (println "Hello," (:first-name author)))

(defn greet-author-2 [{fname :first-name}]
  (println "Hello," fname))

(defn ellipsize [words]
  (let [[w1 w2 w3] (str/split words #"\s+")]
    (str/join " " [w1 w2 w3 "..."])))

;(.exists (File. "/tmp"))

;(def rnd (new java.util.Random))

;(. Math PI)

(defn is-small? [number]
  (if (< number 100) "yes" "no"))

(defn is-small? [number]
  (if (< number 100)
    "yes"
    (do
      (println "Saw a big number" number)
      "no")))

(defn demo-loop []
  (loop [result [] x 5]
    (if (zero? x)
      result
      (recur (conj result x) (dec x)))))

(defn countdown [result x]
  (if (zero? x)
    result
    (recur (conj result x) (dec x))))

(defn indexed [coll]
  (map-indexed vector coll))

(defn index-filter [pred coll]
  (when pred
    (for [[idx elt] (indexed coll) :when (pred elt)] idx)))

(defn index-of-any [pred coll]
  (first (index-filter pred coll)))

(defn ^{:tag String} shout [^{:tag String} s]
  (.toUpperCase s))
; ^Classname -> ^{:tag Classname} へと展開される
(defn ^String shout [^String s]
  (.toUpperCase s))
; メタデータは後ろにも書ける
(defn shout
  ([s] (.toUpperCase s))
  {:tag String})

(meta #'shout)