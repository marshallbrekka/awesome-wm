(ns awesome-wm.api.monitors.impl
  (:require [awesome-wm.api.events :as events]
            [awesome-wm.api.monitors.proto :refer [iMonitors]]
            [goog.object :as obj]))

(defrecord JSMonitors []
  iMonitors
  (monitors [_]
    (-> (.monitors (obj/get js/aw "monitors"))
        (js->clj :keywordize-keys true)))
  (add-listener [_ f]
    (events/add-event-listener "aw.monitors.layoutChange" f))
  (remove-listener [_ f]
    (events/remove-event-listener "aw.monitors.layoutChange" f)))

(defn construct []
  (JSMonitors.))
