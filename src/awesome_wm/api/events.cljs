(ns awesome-wm.api.events
  (:require [goog.object :as obj]))

(defn add-event-listener [event f]
  (.log js/console "add-event-listener" event f)
  (.addEvent (obj/get js/aw "events") event f))

(defn remove-event-listener [event f]
  (.removeEvent (obj/get js/aw "events") event f))
