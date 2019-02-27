;; Manages monitor objects (and the list of monitors)
(ns awesome-wm.model.monitors
  (:require [awesome-wm.util.geometry :as geo]
            [awesome-wm.util.log :as log]
            [awesome-wm.model.internal.util :as util]))


(defn rotate-monitor-helper
  "Given a list of monitors, the current 'focused' monitor, and a function that takes
   two arguments (the current index of the focused monitor in the list of all monitors,
   and the length of the list of monitors) and returns a new monitor index, it returns
   the monitor at the index that the new-index-fn returns.

   If the list of monitors only contains a single monitor, it always returns that monitor."
  [monitors monitor new-index-fn]
  (let [length (count monitors)]
    (if (= 1 length)
      monitor
      (let [index (util/index-of monitors monitor)
            new-index (new-index-fn index length)]
        (nth monitors new-index)))))


(defn next-monitor [monitors monitor]
  (rotate-monitor-helper monitors monitor (fn [index length]
                                            (log/log "next" index length)
                                            (if (= index (dec length))
                                              0
                                              (inc index)))))

(defn previous-monitor [monitors monitor]
  (rotate-monitor-helper monitors monitor (fn [index length]
                                            (log/log "previous" index length )
                                            (if (= 0 index)
                                              (dec length)
                                              (dec index)))))
