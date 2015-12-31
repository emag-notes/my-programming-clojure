(ns examples.concurrency
  (:use examples.chat))

(def counter (ref 0))

(defn next-counter [] (dosync (alter counter inc)))

(def counter (agent 0 :validator number?))

(def backup-agent (agent "output/messages-backup.clj"))

(defn add-message-with-backup [msg]
  (dosync
    (let [snapshot (commute messages conj msg)]
      (send-off backup-agent (fn [filename]
                               (spit filename snapshot)
                               filename))
      snapshot)))

(defn ^:dynamic slow-double [n]
  (Thread/sleep 100)
  (* n 2))

(defn calls-slow-double []
  (map slow-double [1 2 1 2 1 2]))

(defn demo-memoize []
  (time
    (dorun
      (binding [slow-double (memoize slow-double)]
        (calls-slow-double)))))