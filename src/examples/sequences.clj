(ns examples.sequences)

;(let [m (re-matcher #"\w+" "the quick brown fox")]
;  (loop [match (re-find m)]
;    (when match
;      (println match)
;      (recur (re-find m)))))

;(re-seq #"\w+" "the quick bronw fox")

(defn minutes-to-millis [mins] (* mins 1000 600))

(defn recently-modified? [file]
  (> (.lastModified file)
     (- (System/currentTimeMillis) (minutes-to-millis 30))))

(:use '[clojure.java.io :only (reader)])
(defn non-blank? [line] (if (re-find #"\S" line) true false))

(defn non-git? [file] (not (.contains (.toString file) ".git")))

(defn clojure-source? [file] (.endsWith (.toString file) ".clj"))

(defn clojure-loc [base-file]
  (reduce
    +
    (for [file (file-seq base-file)
          :when (and (clojure-source? file) (non-git? file))]
      (with-open [rdr (reader file)]
        (count (filter non-blank? (line-seq rdr)))))))
