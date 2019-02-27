(ns awesome-wm.api.cursor.impl
  (:require [awesome-wm.api.cursor.proto :refer [iCursor]]
            [goog.object :as obj]))

(def cursor-obj (obj/get js/aw "cursor"))

(defrecord JSCursor []
  iCursor
  (get-position [_]
    (-> (.getPosition cursor-obj)
        (js->clj :keywordize-keys true)))
  (set-position [_ point]
    (.setPosition cursor-obj (clj->js point)))
  (click [_ point]
    (.click cursor-obj (clj->js point))))

(defn construct []
  (JSCursor.))
