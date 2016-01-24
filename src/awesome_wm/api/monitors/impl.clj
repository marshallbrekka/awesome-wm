(ns awesome-wm.api.monitors.impl
  (:require [awesome-wm.api.monitors.proto :refer [iMonitors]]))

(def listeners (atom #{}))
(def monitors (atom [{:id 1 :frame {:x 0 :y 0 :width 1000 :height 700}}
                     {:id 2 :frame {:x -800 :y 10 :width 800 :height 7000}}]))

(defn- trigger-listeners [k ref old-state new-state]
  (doseq [listener @listeners]
    (listener)))

(defrecord TestMonitors [listeners monitors]
  iMonitors
  (get [_]
    @monitors)
  (add-listener [_ f]
    (swap! listeners conj f))
  (remove-listener [_ f]
    (swap! listeners disj f)))

(defn construct []
  (add-watch monitors :monitors trigger-listeners)
  (TestMonitors. listeners monitors))
