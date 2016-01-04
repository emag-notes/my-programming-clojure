(ns examples.multimethods.default)

(defmulti my-print class :default :everything-else)

(defmethod my-print String [s]
  (.write *out* s))

(defmethod my-print :everything-else [_]
  (.write *out* "Not implemented yet..."))

(defn my-println [ob]
  (my-print ob)
  (.write *out* "\n"))