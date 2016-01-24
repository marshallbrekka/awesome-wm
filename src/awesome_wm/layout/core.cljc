(ns awesome-wm.layout.core
  (:require [awesome-wm.api.windows.core :as win-api]
            [awesome-wm.layout.simple :as simple]))

(def strategies {:simple simple/layout})

(defn apply-layout [windows frames]
  (->> (map win-api/set-frame windows frames)
       (doall)))

(defn layout [windows monitor strategy]
  (->> (count windows)
       ((get strategies strategy) (:frame monitor))
       (apply-layout windows)))
