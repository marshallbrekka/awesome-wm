(ns awesome-wm.layout.simple
  (:require [awesome-wm.util.math :as math]))

(defn layout [frame window-count]
  (let [width (-> (:width frame)
                  (/ window-count)
                  (double)
                  (math/floor))]
    (for [n (take window-count (iterate inc 0))]
      (assoc frame
             :width (-> n inc (* width))
             :x     (+ (:x frame) (* width n))))))
