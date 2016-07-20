(ns awesome-wm.layout.core
  (:require [awesome-wm.api.windows.core :as win-api]
            [awesome-wm.layout.max :as max]))

(def strategies {:simple (max/->Max)})

(defn apply-layout [windows frames]
  (->> (map win-api/set-frame windows frames)
       (doall)))

(defn layout [windows monitor strategy]
  (->> (count windows)
       ((get strategies strategy) (:frame monitor))
       (apply-layout windows)))
