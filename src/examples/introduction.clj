(ns examples.introduction)

(defn blank? [str]
  (every? #(Character/isWhitespace %) str))
