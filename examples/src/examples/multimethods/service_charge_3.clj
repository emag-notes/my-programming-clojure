(ns examples.multimethods.service-charge-3
  (:require examples.multimethods.account))

(in-ns 'examples.multimethods.account)
(clojure.core/use 'clojure.core)

(derive ::acc/Savings ::acc/Account)
(derive ::acc/Checking ::acc/Account)

(defmulti service-charge (fn [acct] [(account-level acct) (:tag acct)]))

(defmethod service-charge [::acc/Basic ::acc/Checking]   [_] 25)
(defmethod service-charge [::acc/Basic ::acc/Savings]    [_] 10)
(defmethod service-charge [::acc/Premium ::acc/Account]  [_] 0)