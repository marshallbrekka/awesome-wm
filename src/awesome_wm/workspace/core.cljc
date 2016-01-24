;; Code for managing a workspace state.
;; (def demo-structure
;;   { ;; store the ordered list of windows for the workspace here
;;    :windows [{:pid 123 :id 789} {:pid 123 :id 456}]
;;    ;; reference the focused window id
;;    :focused 789
;;    ;; the current layout strategy, its varient setting, and its column settings
;;    :layout {:strategy :simple
;;             :varient  :none
;;             :column   :who-knows? ;; TODO research what awesome calls this
;;             }
;;    ;; indicates if the workspace is in fullscreen mode, in which case the focused
;;    ;; window takes up the entire screen
;;    :full-screen false})

(ns awesome-wm.workspace.core
  (:require [awesome-wm.api.windows.core :as win-api]
            [awesome-wm.api.applications.core :as app-api]
            [awesome-wm.model.internal.util :as util]))



(defn- nearest-index [length index]
  (when (not= 0 length)
    (if (= 0 index)
      index
      (dec index))))

(defn make-workspace []
  {:windows []
   :focused nil
   :layout {:strategy :none}
   :full-screen false})

(defn add-window [ws window]
  (-> ws
      (update :windows conj window)
      (update :focused window)))

(defn remove-window [workspace window]
  (let [windows (->> (:windows workspace)
                     (filter #(not= % window))
                     (vec))
        index (util/index-of (:windows workspace) window)
        new-focus-index (nearest-index (count windows) index)]
    (assoc workspace
           :windows windows
           :focused (some->> new-focus-index (nth windows)))))

(defn focus-window [workspace window]
  (assoc workspace :focused window))

(defn rotate-focused-window-helper [workspace new-index-fn]
  (let [windows (:windows workspace)
        length (count windows)]
    (if (>= 1 length)
      workspace
      (let [index (util/index-of (:windows workspace) (:focused workspace))
            new-index (new-index-fn index length)]
        (update workspace :windows assoc
                index (nth windows new-index)
                new-index (nth windows index))))))

(defn rotate-focused-window-forward [workspace]
  (rotate-focused-window-helper workspace
                                (fn [index length]
                                  (if (= 0 index)
                                    (dec length)
                                    (dec index))))
  )

(defn rotate-focused-window-backward [workspace]
  (rotate-focused-window-helper workspace
                                (fn [index length]
                                  (if (= index (dec length))
                                    0
                                    (inc index)))))

(defn focus-window [window workspace-windows]
  (app-api/activate window)
  (doseq [win workspace-windows]
    (win-api/become-main win))
  (win-api/become-main window))

(defn test-focus []
  (let [windows (win-api/get)]
    (focus-window (second windows) windows)))
