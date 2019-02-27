(ns awesome-wm.api.monitors.impl
  (:require [awesome-wm.api.events :as events]
            [awesome-wm.api.monitors.proto :refer [iMonitors]]
            [goog.object :as obj]))

;; (defrecord JSMonitors []
;;   iMonitors
;;   (monitors [_]
;;     (-> (.monitors (js/aw "monitors"))
;;         (js->clj :keywordize-keys true)))
;;   (add-listener [_ f]
;;     (events/add-event-listener "aw.monitors.layoutChange" f))
;;   (remove-listener [_ f]
;;     (events/remove-event-listener "aw.monitors.layoutChange" f)))

(defrecord PhoenixMonitors []
  iMonitors
  (monitors [_]
    (->> (. js/Screen all)
         (map (fn [m] {:id (.hash m)
                       :frame (js->clj (.flippedVisibleFrame m) :keywordize-keys true)}))))
  (add-listener [_ f] nil)

  (remove-listener [_ f] nil))


(defn construct []
  (PhoenixMonitors.))
