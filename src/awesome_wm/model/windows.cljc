(ns awesome-wm.model.windows
  (:require [awesome-wm.util.geometry :as geo]))

(defn current-monitor
  "Given the frame of a window and an array of monitor objects with keys
   :id and :frame, returns the monitor object that contains the majority
   of the window frame."
  [frame monitors]
  (->> monitors
       (reduce (fn [largest monitor]
                 (if-let [intersection (geo/intersection-rect frame (:frame monitor))]
                   (let [area (* (:width intersection) (:height intersection))]
                     (if (> area (:area largest))
                       {:area area :monitor monitor}
                       largest))
                   largest))
               {:area 0 :monitor nil})
       (:monitor)))
