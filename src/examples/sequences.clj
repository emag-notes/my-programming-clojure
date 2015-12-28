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