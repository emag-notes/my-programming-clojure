(ns examples.sequences
  (:use clojure.xml clojure.set))

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

(use '[clojure.java.io :only (reader)])
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

(def composers
  (for [x (xml-seq
            (parse (java.io.File. "data/sequences/compositions.xml")))
        :when (= :composition (:tag x))]
    (:composer (:attrs x))))

(def song {:name "Agunus Del"
           :artist "Krzysztof Pendercki"
           :album "Polish Requiem"
           :genre "Classical"})

(def languages #{"java" "c" "d" "clojure"})
(def beverages #{"java" "chai" "pop"})

(def compositions
  #{{:name "The Art of the Fugue" :composer "J. S. Bach"}
    {:name "Musical Offering" :composer "J. S. Bach"}
    {:name "Requiem" :composer "Giuseppe Verdi"}
    {:name "Requiem" :composer "W. A. Mozart"}})
(def composers
  #{{:composer "J. S. Bach" :country "Germany"}
    {:composer "W. A. Mozart" :country "Austria"}
    {:composer "Giuseppe Verdi" :country "Italy"}})
(def nations
  #{{:nation "Germany" :language "German"}
    {:nation "Austria" :language "German"}
    {:nation "Italy" :language "Italian"}})