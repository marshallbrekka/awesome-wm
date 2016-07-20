;; Code for managing a workspace state.
;; (def demo-structure
;;   {;; store the ordered list of windows for the workspace here
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

(defn- nearest-index
  "Given a length and index, if the length is = 0 returns nil,
   otherwise returns 0 or index - 1, whichever is greater."
  [length index]
  (when (not= 0 length)
    (max 0 (dec index))))

(defn make-workspace
  "Creates a new blank workspace."
  []
  {:windows []
   :focused nil
   :layout {:strategy :none}
   :full-screen false})

(defn focus-window
  "Given a workspace and a window, sets the given window as the focused window
   for the workspace."
  [workspace window]
  (assoc workspace :focused window))

(defn add-window
  "Given a worspace and a window, adds the window to the list of windows
   for the workspace and sets it as the focused window.
   Returns the modified workspace."
  [workspace window]
  (-> workspace
      (update :windows conj window)
      (focus-window window)))

(defn remove-window
  "Given a workspace and a window, removes the window from the workspace,
   and sets a new :focused window on the workspace (if any windows remain).
   Returns the modified workspace."
  [workspace window]
  (let [windows (->> (:windows workspace)
                     (filter #(not= (:id %) (:id window)))
                     (vec))
        new-workspace (assoc workspace :windows windows)]
    (if (not= (:id window)
              (-> workspace :focused :id))
      new-workspace
      (let [index (util/index-of (:windows workspace) window)
            new-focus-index (nearest-index (count windows) index)]
        (assoc new-workspace :focused (some->> new-focus-index (nth windows)))))))



(defn- rotate-focused-window-helper
  "Given a workspace and a fn that takes the index of the currently focused
   window and the length of all windows and returns a new index,
   re-orderes the list of windows such that the focused window it positioned
   at the new index value.
   Returns the modified workspace."
  [workspace new-index-fn]
  (let [windows (:windows workspace)
        length (count windows)]
    (if (<= length 1)
      workspace
      (let [index (util/index-of (:windows workspace) (:focused workspace))
            new-index (new-index-fn index)]
        (->> (fn [windows]
               (cond
                 (< new-index 0)
                 (-> (rest windows)
                     (vec)
                     (conj (first windows))
                     (vec))

                 (>= new-index length)
                 (->> (pop windows)
                      (cons (last windows))
                      (vec))

                 :else
                 (assoc windows
                        index (nth windows new-index)
                        new-index (nth windows index))))
             (update workspace :windows))))))

(defn rotate-focused-window-forward
  "Given a workspace moves the currently focused window forward (closer to
   index 0) in the list of windows. If the focused window was already at index
   0 then wraps around and puts it at the end of the list of windows.
   Returns the modified workspace."
  [workspace]
  (rotate-focused-window-helper workspace dec))

(defn rotate-focused-window-backward
  "Given a workspace moves the currently focused window backward (closer to the
   end of the list of windows) in the list of windows. If the focused window was
   already at the last index in the list, then wraps around and puts it at the
   start of thelist of windows.
   Returns the modified workspace."
  [workspace]
  (rotate-focused-window-helper workspace inc))

;; (defn focus-window
;;   "Given a window object and a list of windows in the current workspace,
;;    brings provided window to the front and focuses it."
;;   [window workspace-windows]
;;   (app-api/activate window)
;;   (doseq [win workspace-windows]
;;     (win-api/become-main win))
;;   (win-api/become-main window))

;; (defn test-focus []
;;   (let [windows (win-api/get)]
;;     (focus-window (second windows) windows)))
