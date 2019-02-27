(ns awesome-wm.core
  (:require 
   [awesome-wm.api.hotkey.core :as hk]
   ;; [awesome-wm.api.applications.core :as apps]
   [awesome-wm.api.windows.core :as win-api]
   ;; [awesome-wm.api.applications.core :as app-api]
   [awesome-wm.api.cursor.core :as cursor-api]
   [awesome-wm.api.monitors.core :as mon-api]
   [awesome-wm.model.windows :as win]
   [awesome-wm.model.monitors :as monitors]
   ;; [awesome-wm.layout.core :as layout]
   ;; [awesome-wm.workspace.core :as workspace]
   [awesome-wm.util.log :as log]
   #?@(:cljs
       [
        ;; [clojure.browser.repl :as crepl]
        ;; [weasel.repl :as repl]
        ]
       :clj
       [[weasel.repl.websocket :as ws]
        [cemerick.piggieback :as piggieback]])))

;; {:monitor-count 2
;;  :counts {2 {:monitors [{:id 1
;;                          :frame {:x :y :width :height}
;;                          :workspace {}}
;;                         {:id 2
;;                          :frame {:x :y :width :height}
;;                          :workspace {}}]
;;              :active 0}}}
;; (def _state (atom nil))

;; (defn save-state [new-state]
;;   (reset! _state new-state))

;; (defn assign-window-helper [monitor-list window window-monitor])

;; (defn assign-window [monitor-list window]
;;   (let [win-monitor (win/current-monitor (win-api/get-frame window) monitor-list)]
;;     (->> monitor-list
;;          (map (fn [monitor]
;;                 (if (= (:id monitor) (:id win-monitor))
;;                   (update monitor :workspace workspace/add-window window)
;;                   monitor))))))

;; (defn initialize-monitors [new-monitors]
;;   (->> (win-api/windows)
;;        (reduce assign-window new-monitors)
;;        (assoc {:active 0} :monitors)))

;; (defn nearest-monitors [state monitor-count]
;;   (when-not (-> state :counts empty?)
;;     (->> (:counts state)
;;          (keys)
;;          (reduce (fn [nearest-count next-count]
;;                    (let [abs-nearest (math/abs (- monitor-count nearest-count))
;;                          abs-next (math/abs (- monitor-count next-count))]
;;                      (if (or (< abs-nearest abs-next)
;;                              (and (= abs-nearest abs-next)
;;                                   (< nearest-count next-count)))
;;                        nearest-count
;;                        next-count))))
;;          (get (:counts state)))))


;; (defn resize-monitor
;;   "Given a list of monitors that are nearest in size to the list of the new monitors,
;;    copies the workspaces from the existing monitors to the new monitors.

;;    If their are more new monitors than the existing ones, each extra monitor recieves a blank workspace.
;;    If their are more existing monitors than new ones, then each extra workspace is merged with
;;    the first monitors workspace." 
;;   [nearest-monitors new-monitors]
;;   (->> (max (count nearest-monitors)
;;             (count new-monitors))
;;        (range)
;;        (reduce (fn [monitors index]
;;                  (let [new-workspace (or (some-> nearest-monitors (get index) :workspace)
;;                                          (workspace/make-workspace))]
;;                    ;; If have a monitor in both old and new, then assign the old monitor workspace
;;                    ;; to the new monitor, otherwise, we have more old monitors, so we merge all
;;                    ;; remaining workspaces into the first monitors workspaces.
;;                    (if (< index (count new-monitors))
;;                      (->> new-workspace
;;                           (assoc (get new-monitors index) :workspace)
;;                           (assoc monitors index))
;;                      (->> (first new-monitors)
;;                           (update-in monitors [0 :workspace] workspace/merge-workspace new-workspace)))))
;;                {})))

;; (defn create-new-monitors [state new-monitors]
;;   (->> (or (some-> (nearest-monitors state (count new-monitors))
;;                    (resize-monitor new-monitors))
;;            (initialize-monitors new-monitors))
;;        (assoc-in state [:counts (count new-monitors)])))

;; (defn monitors-changed [state new-monitors]
;;   (let [c (count new-monitors)]
;;     (-> (if (-> state :counts (get c) nil?)
;;           (create-new-monitors state new-monitors)
;;           state)
;;         (assoc :monitor-count c))))

;; (defn perform-layout! [new-state old-state]
;;   (let [active-monitors (-> new-state
;;                             :counts
;;                             (get (:monitor-count new-state)))
;;         active-workspace (-> active-monitors
;;                              (:monitors)
;;                              (vec)
;;                              (nth (:active active-monitors))
;;                              (:workspace))]
;;     (some-> active-workspace :focused app-api/activate)
;;     (some-> active-workspace :focused win-api/become-main))
;;   new-state)

;; (defn monitors-changed-handler
;;   "Event handler for the aw.monitors.layoutChange event.
;;    Takes a list of monitor objects, and "
;;   [evt new-monitors]
;;   (let [old-state @_state]
;;     (-> (monitors-changed old-state new-monitors)
;;         (perform-layout! old-state)
;;         (save-state))))

;; (defn initialize-everything []
;;   (-> (monitors-changed {} (mon-api/monitors))
;;       (perform-layout! {})
;;       (save-state)))

;; (defn focus-window-in-monitors
;;   [monitors focus-fn]
;;   (-> monitors
;;       (update :monitors vec)
;;       (update-in [:monitors (:active monitors) :workspace] focus-fn)))

;; (defn focus-window-helper [state focus-fn]
;;   (update-in state
;;              [:counts (:monitor-count state)]
;;              focus-window-in-monitors focus-fn))

;; (defn next-window [& args]
;;   (let [state @_state]
;;     (-> (focus-window-helper state workspace/focus-next-window)
;;         (perform-layout! state)
;;         (save-state))))

;; (defn previous-window [& args]
;;   (let [state @_state]
;;     (-> (focus-window-helper state workspace/focus-previous-window)
;;         (perform-layout! state)
;;         (save-state))))

;; (initialize-everything)

;; (hk/add "k" ["cmd" "opt"] next-window)
;; (hk/add "j" ["cmd" "opt"] previous-window)





;;;;;;; Begin Slate Config ;;;;;;;;
(defn frame-multiple
  [monitor-divider position-index frame]
  (let [width (-> (:width frame)
                  (/ monitor-divider)
                  (double))]
    (-> frame
        (assoc :width (int (Math/floor width)))
        (update :x + (int (Math/round (* width position-index)))))))

(defn monitor-middleware [monitors]
  (->> monitors
       (reduce (fn [monitors monitor]
                 ;; Split my combined monitor
                 (if (-> monitor :frame :width (> 1920))
                   (conj monitors
                         (update monitor :frame #(frame-multiple 2 0 %))
                         (update monitor :frame #(frame-multiple 2 1 %)))
                   (conj monitors monitor)))
               [])
       (sort-by (fn [mon] (-> mon :frame :x)) <)
       ))

(defn get-monitors []
  (-> (mon-api/monitors)
      (monitor-middleware)
      ))

(defn adjust-focused-window [frame-adjust-fn]
  (if-let [window (win-api/focused)]
    (->> (get-monitors)
         (win/current-monitor (win-api/get-frame window))
         (:frame)
         (frame-adjust-fn)
         (win-api/set-frame window))))

(defn full-screen []
  (adjust-focused-window identity))

(defn half-screen-left []
  (adjust-focused-window (partial frame-multiple 2 0)))

(defn half-screen-right []
  (adjust-focused-window (partial frame-multiple 2 1)))

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


(defn focus-monitor [index x-offset y-offset]
  (let [monitors (vec (get-monitors))]
    (when (< index (count monitors))
      (let [point (-> (nth monitors index)
                      (:frame)
                      (dissoc :width :height)
                      (update :x + x-offset)
                      (update :y + y-offset))]
        (cursor-api/set-position point)
        (cursor-api/click point)))))

(def x-cursor-offset 80)
(def y-cursor-offset 10)

(log/log "about to register handlers")

(hk/add "f" ["cmd" "alt"] full-screen)
(hk/add "left" ["cmd" "alt"] half-screen-left)
(hk/add "right" ["cmd" "alt"] half-screen-right)
(hk/add "right" ["cmd" "alt" "ctrl"] next-monitor)
(hk/add "left" ["cmd" "alt" "ctrl"] previous-monitor)
(hk/add "," ["cmd", "alt"]
        (fn []
          (adjust-focused-window (partial frame-multiple 3 0))))
(hk/add "." ["cmd", "alt"]
        (fn []
          (adjust-focused-window (partial frame-multiple 3 1))))
(hk/add "/" ["cmd", "alt"]
        (fn []
          (adjust-focused-window (partial frame-multiple 3 2))))

(hk/add "m" ["ctrl"] #(focus-monitor 0 x-cursor-offset y-cursor-offset))
(hk/add "," ["ctrl"] #(focus-monitor 1 x-cursor-offset y-cursor-offset))
(hk/add "." ["ctrl"] #(focus-monitor 2 x-cursor-offset y-cursor-offset))
(hk/add "/" ["ctrl"] #(focus-monitor 3 x-cursor-offset y-cursor-offset))

;;;;;;;;;; End Slate Config ;;;;;;;;;;;


;; ;; #?(:cljs
;; ;;    (do
;; ;;      (defonce conn
;; ;;        (crepl/connect "http://localhost:9000/repl")) 

;; ;;      (enable-console-print!)

;; ;;      (println "Hello world!")))

;; ;; REPL setup code
;; ;; #?(:cljs 
;; ;;    (do
;; ;;      (enable-console-print!)
;; ;;      (.log js/console "hello test")

;; ;;      (when-not (repl/alive?)
;; ;;        (repl/connect "ws://localhost:9001"))

;; ;;      (if (repl/alive?)
;; ;;        (println "Loaded example"))

;; ;;      (repl/alive?))
;; ;;    :clj
;; ;;    (defn start-cljs-repl []
;; ;;      (piggieback/cljs-repl
;; ;;       (ws/repl-env :ip "0.0.0.0" :port 9001))))


