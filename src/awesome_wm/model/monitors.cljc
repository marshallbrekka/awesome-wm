(ns awesome-wm.model.monitors
  (:require [awesome-wm.util.geometry :as geo]
            [awesome-wm.model.internal.util :as util]))


(defn rotate-monitor-helper [monitors monitor new-index-fn]
  (let [length (count monitors)]
    (if (= 1 length)
      monitor
      (let [index (util/index-of monitors monitor)
            new-index (new-index-fn index length)]
        (nth monitors new-index)))))


(defn next-monitor [monitors monitor]
  (rotate-monitor-helper monitors monitor (fn [index length]
                                            (println "next" index length )
                                            (if (= index (dec length))
                                              0
                                              (inc index)))))

(defn previous-monitor [monitors monitor]
  (rotate-monitor-helper monitors monitor (fn [index length]
                                            (println "previous" index length )
                                            (if (= 0 index)
                                              (dec length)
                                              (dec index)))))
