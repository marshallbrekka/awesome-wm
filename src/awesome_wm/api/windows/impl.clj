(ns awesome-wm.api.windows.impl
  (:require [awesome-wm.api.windows.proto :refer [iWindows]]))

(def windows (atom []))

(defrecord TestWindows [listeners windows]
  iWindows
  (windows [_]
    @windows)
  (close [_ pid id]
    nil)
  (focused [_]
    (first @windows))
  (become-main [_ pid id]
    nil)
  (set-frame [_ pid id frame]
    nil)
  (get-frame [_ pid id]
    {:x 10 :y 0 :width 100 :height 200})
  (set-minimized [_ pid id minimize?]
    nil)
  (add-listener [_ event f] nil)
  (remove-listener [_ event f] nil))

(defn construct []
  (TestWindows. nil windows))
