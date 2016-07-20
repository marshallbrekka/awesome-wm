;; Entry point for most layout operations.
;; Montitors contain workspaces/tags, which themselves contain
;; the actual windows and their layout settings.
;; When a window is created or removed, the notification will be
;; handled by the monitors first, which will then delegate to
;; the appropriate monitor/workspace/tag.
(ns awesome-wm.monitors
  (:require [awesome-wm.api.monitors.core :as monitor-api]))

(def ^:dynamic *state* (atom {}))

(defn half-frame-left [frame]
  (update frame :width (fn [width] (/ width 2))))

(defn half-frame-right [frame]
  (-> frame
      (update :x #(+ % (/ (:width frame) 2)))
      (half-frame-left)))

(defn monitor-middleware [monitors]
  (->> monitors
       (reduce (fn [monitors monitor]
                 (let [monitor (update monitor :id str)]
                   ;; Split my combined monitor
                   (if (-> monitor :frame :width (> 1920))
                     (conj monitors
                           (-> monitor
                               (update :frame half-frame-left)
                               (update :id str "-1"))
                           (-> monitor
                               (update :frame half-frame-right)
                               (update :id str "-2")))
                     (conj monitors monitor))))
               [])))

(defn get-monitors []
  (-> (monitor-api/get)
      (monitor-middleware)))

(defn count-exists? [existing-state monitors]
  (contains? existing-state (count monitors)))

(defn init-layout [existing-state monitors]
  (if (count-exists? existing-state monitors)
    existing-state
    ()))


(defn layout-change-listener []

  )

(defn init []
  )
