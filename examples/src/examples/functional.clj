(ns examples.functional)

(defn stack-consuming-fibo [n]
  (cond
    (= n 0) 0
    (= n 1) 1
  :else (+ (stack-consuming-fibo (- n 1))
           (stack-consuming-fibo (- n 2)))))

(defn tail-fibo [n]
  (letfn [(fib
            [current next n]
            (if (zero? n)
              current
              (fib next (+ current next) (dec n))))]
    (fib 0N 1N n)))

(defn recur-fibo [n]
  (letfn [(fib
            [current next n]
            (if (zero? n)
              current
              (recur next (+ current next) (dec n))))]
    (fib 0N 1N n)))

(defn lazy-seq-fibo
  ([]
    (concat [0 1] (lazy-seq-fibo 0N 1N)))
  ([a b]
    (let [n (+ a b)]
      (lazy-seq
        (cons n (lazy-seq-fibo b n))))))

(defn fibo []
  (map first (iterate (fn [[a b]] [b (+ a b)]) [0N 1N])))

(def head-fibo
  (lazy-cat [0N 1N]
            (map + head-fibo (rest head-fibo))))

(defn count-heads-pairs [coll]
  (loop [cnt 0 coll coll]
    (if (empty? coll)
      cnt
      (recur (if (= :h (first coll) (second coll))
               (inc cnt)
               cnt)
             (rest coll)))))

(defn by-pairs [coll]
  (let [take-pair (fn [c]
                    (when (next c) (take 2 c)))]
    (lazy-seq
      (when-let [pair (seq (take-pair coll))]
        (cons pair (by-pairs (rest coll)))))))

(defn count-heads-pairs [coll]
  (count (filter (fn [pair] (every? #(= :h %) pair))
                 (by-pairs coll))))

(defn count-heads-pairs [coll]
  (count (filter (fn [pair] (every? #(= :h %) pair))
                 (partition 2 1 coll))))

(def ^{:doc "Count items matching a filter"}
  count-if (comp count filter))

(defn count-runs
  "Count runs of length n where pred is true in coll."
  [n pred coll]
  (count-if #(every? pred %) (partition n 1 coll)))

(def ^{:doc "Count runs of length two that are both heads"}
  count-heads-pairs (partial count-runs 2 #(= % :h)))

(defn faux-curry [& args] (apply partial partial args))

(def add-3 (partial + 3))
(def add-3 ((faux-curry +) 3))

(declare my-odd? my-even?)

(defn my-odd? [n]
  (if (= n 0)
    false
    (my-even? (dec n))))

(defn my-even? [n]
  (if (= n 0)
    true
    (my-odd? (dec n))))

(defn parity [n]
  (loop [n n par 0]
    (if (= n 0)
      par
      (recur (dec n) (- 1 par)))))

(defn my-even? [n]
  (= 0 (parity n)))

(defn my-odd? [n]
  (= 1 (parity n)))

(defn trampoline-fibo [n]
  (let [fib (fn fib [f-2 f-1 current]
              (let [f (+ f-2 f-1)]
                (if (= n current)
                  f
                  #(fib f-1 f (inc current)))))]
    (cond
      (= n 0) 0
      (= n 1) 1
      :else (fib 0N 1 2))))

(defn my-odd? [n]
  (if (= n 0)
    false
    #(my-even? (dec n))))

(defn my-even? [n]
  (if (= n 0)
    true
    #(my-odd? (dec n))))

(declare replace-symbol replace-symbol-expression)
(defn replace-symbol [coll oldsym newsym]
  (if (empty? coll)
    ()
    (cons (replace-symbol-expression
            (first coll) oldsym newsym)
          (replace-symbol
            (rest coll) oldsym newsym))))

(defn replace-symbol-expression [symbol-expr oldsym newsym]
  (if (symbol? symbol-expr)
    (if (= symbol-expr oldsym)
      newsym
      symbol-expr)
    (replace-symbol symbol-expr oldsym newsym)))

(defn deeply-nested [n]
  (loop [n n
         result '(bottom)]
    (if (= n 0)
      result
      (recur (dec n) (list result)))))

(defn- coll-or-scalar [x & _]
  (if (coll? x)
    :collection :scalar))
(defmulti replace-symbol coll-or-scalar)
(defmethod replace-symbol :collection [coll oldsym newsym]
  (lazy-seq
    (when (seq coll)
      (cons (replace-symbol (first coll) oldsym newsym)
            (replace-symbol (rest coll) oldsym newsym)))))
(defmethod replace-symbol :scalar [obj oldsym newsym]
  (if (= obj oldsym) newsym obj))