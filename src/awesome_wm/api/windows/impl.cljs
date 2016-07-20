(ns awesome-wm.api.windows.impl
  (:require [awesome-wm.api.events :as events]
            [awesome-wm.api.windows.proto :refer [iWindows]]
            [goog.object :as obj]))

(def window-obj (obj/get js/aw "window"))
(defn ->event-name [event]
  (str "aw.window." event))

(defrecord JSWindows []
  iWindows
  (get [_]
    (-> (.windows window-obj)
        (js->clj :keywordize-keys true)))
  (close [_ pid id]
    (.close window-obj pid id))
  (focused [_]
    (-> (.focusedWindow window-obj)
        (#(do (.log js/console %)
              %))
        (js->clj :keywordize-keys true)
        (#(do (.log js/console (first (keys %))) %))))
  (become-main [_ pid id]
    (.becomeMain window-obj pid id))
  (set-frame [_ pid id frame]
    (.setFrame window-obj pid id (clj->js frame)))
  (get-frame [_ pid id]
    (-> (.getFrame window-obj pid id)
        (js->clj :keywordize-keys true)))
  (set-minimized [_ pid id minimize?]
    (.setMinimized window-obj pid id minimize?))
  (add-listener [_ event f]
    (events/add-event-listener (->event-name event) f))
  (remove-listener [_ event f]
    (events/remove-event-listener (->event-name event) f)))

(defn construct []
  (JSWindows.))
