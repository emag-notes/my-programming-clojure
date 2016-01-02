(ns examples.macro)

(defn unless [expr form]
  (if expr nil form))

(defn unless [expr form]
  (println "About to test...")
  (if expr nil form))

(defmacro unless [expr form]
  (list 'if expr nil form))

(defmacro bad-unless [expr form]
  (list 'if 'expr nil form))

(defmacro chain
  ([x form] (list '. x form))
  ([x form & more] (concat (list 'chain (list '. x form)) more)))

(defmacro chain
  ([x form] `(. ~x ~form))
  ([x form & more] `(chain (. ~x ~form) ~@more)))

(defmacro bench [expr]
  `(let [start# (System/nanoTime)
         result# ~expr]
     {:result result# :elapsed (- (System/nanoTime) start#)}))