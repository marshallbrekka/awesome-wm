(ns awesome-wm.core
  (:require [awesome-wm.api.hotkey.core :as hk]
            [awesome-wm.api.applications.core :as apps]
            [awesome-wm.api.windows.core :as win-api]
            [awesome-wm.api.monitors.core :as mon-api]
            [awesome-wm.model.windows :as win]
            [awesome-wm.model.monitors :as monitors]
            [awesome-wm.layout.core :as layout]
            [awesome-wm.workspace.core :as workspace]
            [awesome-wm.util.log :as log]
            #?@(:cljs
                [[weasel.repl :as repl]]
                :clj
                [[weasel.repl.websocket :as ws]
                 [cemerick.piggieback :as piggieback]])))

;;;;;;; Begin Slate Config ;;;;;;;;
(defn half-frame-left [frame]
  (update frame :width (fn [width] (/ width 2))))

(defn half-frame-right [frame]
  (-> frame
      (update :x #(+ % (/ (:width frame) 2)))
      (half-frame-left)))

(defn monitor-middleware [monitors]
  (->> monitors
       (reduce (fn [monitors monitor]
                 ;; Split my combined monitor
                 (if (-> monitor :frame :width (> 1920))
                   (conj monitors
                         (update monitor :frame half-frame-left)
                         (update monitor :frame half-frame-right))
                   (conj monitors monitor)))
               [])))

(defn get-monitors []
  (-> (mon-api/get)
      (monitor-middleware)))

(defn full-screen []
  (if-let [window (win-api/focused)]
    (->> (get-monitors)
         (win/current-monitor (win-api/get-frame window))
         (:frame)
         (win-api/set-frame window))))

(defn half-screen-left []
  (if-let [window (win-api/focused)]
    (->> (get-monitors)
         (win/current-monitor (win-api/get-frame window))
         (:frame)
         (half-frame-left)
         (win-api/set-frame window))))

(defn half-screen-right []
  (if-let [window (win-api/focused)]
    (->> (get-monitors)
         (win/current-monitor (win-api/get-frame window))
         (:frame)
         (half-frame-right)
         (win-api/set-frame window))))

(defn next-monitor []
  (if-let [window (win-api/focused)]
    (let [monitors (get-monitors)]
      (->> monitors
           (win/current-monitor (win-api/get-frame window))
           (monitors/next-monitor monitors)
           (:frame)
           (win-api/set-frame window)))))

(defn previous-monitor []
  (if-let [window (win-api/focused)]
    (let [monitors (get-monitors)]
      (->> monitors
           (win/current-monitor (win-api/get-frame window))
           (monitors/previous-monitor monitors)
           (:frame)
           (win-api/set-frame window)))))

(hk/add "f" ["cmd" "opt"] full-screen)
(hk/add "left" ["cmd" "opt"] half-screen-left)
(hk/add "right" ["cmd" "opt"] half-screen-right)
(hk/add "right" ["cmd" "opt" "ctrl"] next-monitor)
(hk/add "left" ["cmd" "opt" "ctrl"] previous-monitor)

;;;;;;;;;; End Slate Config ;;;;;;;;;;;


;; REPL setup code
#?(:cljs
   (do
     (enable-console-print!)
     (.log js/console "hello test")

     (when-not (repl/alive?)
       (repl/connect "ws://localhost:9001"))

     (if (repl/alive?)
       (println "Loaded example"))

     (repl/alive?))
   :clj
   (defn start-cljs-repl []
     (piggieback/cljs-repl
      (ws/repl-env :ip "0.0.0.0" :port 9001))))


