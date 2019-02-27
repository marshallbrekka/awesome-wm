(ns awesome-wm.api.cursor.impl
  (:require [awesome-wm.api.cursor.proto :refer [iCursor]]))

(defrecord TestCursor []
  iCursor
  (get-position [_]
    {:x 0 :y 0})
  (set-position [_ point]
    true)
  (click [_ point]
    true))

(defn construct []
  (TestCursor.))
