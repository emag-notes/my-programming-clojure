(ns clojurebreaker.views.welcome
  (:require [clojurebreaker.views.common :as common]
            [clojurebreaker.models.game :as game]
            [noir.session :as session])
  (:use [noir.core :only [defpartial defpage render]]
        [hiccup.form :only (form-to text-field submit-button)]))

(defpartial board [{:keys [one two three four exact unordered]}]
            (when (and exact unordered)
              [:div "Exact: " exact " Unordered: " unordered])
            (form-to [:post "/guess"]
                     (text-field "one")
                     (text-field "two")
                     (text-field "three")
                     (text-field "four")
                     (submit-button "Guess")))

(defpage "/" {:as guesses}
         (when-not (session/get :game)
           (session/put! :game (game/create)))
         (common/layout (board (or guesses nil))))

(defpage [:post "/guess"] {:keys [one two three four]}
         (let [result (game/score (session/get :game) [one two three four])]
           (if (= (:exact result) 4)
             (do (session/remove! :game)
                 (common/layout
                   [:h2 "Congratulations, you have solved the puzzle!"]
                   (form-to [:get "/"]
                            (submit-button "Start A New Game"))))
             (do (session/flash-put! "result" result)
                 (render "/" {:one one
                              :two two
                              :three three
                              :four four
                              :exact (:exact result)
                              :unordered (:unordered result)})))))
