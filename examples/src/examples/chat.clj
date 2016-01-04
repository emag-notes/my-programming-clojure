(ns examples.chat)

(defrecord Message [sender text])

(def messages (ref ()))

(defn naive-add-message [msg]
  (dosync (ref-set messages (cons msg @messages))))

(defn add-message [msg]
  (dosync (alter messages conj msg)))

(defn add-message-commute [msg]
  (dosync (commute messages conj msg)))

(def validate-message-list
  (partial every? #(and (:sender %) (:text %))))

(def messages (ref () :validator validate-message-list))