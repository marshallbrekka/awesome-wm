(ns awesome-wm.layout.max
  (:require [awesome-wm.layout.proto :refer [ILayout]]))

(defn arrange [workspace screen-frame workarea-frame _ _]
  (->> (repeat workarea-frame)
       (take (count (:windows workspace)))))

(defrecord Max []
  ILayout
  (arrange [_ workspace screen-frame workarea-frame _ _]
    (arrange workspace screen-frame workarea-frame _ _)))
