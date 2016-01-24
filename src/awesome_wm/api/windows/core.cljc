(ns awesome-wm.api.windows.core
  (:require [awesome-wm.api.windows.impl :as impl]
            [awesome-wm.api.windows.proto :as p]))

(def ^:dynamic *impl* (delay (impl/construct)))

(defn get []
  (p/get @*impl*))

(defn close [window]
  (p/close @*impl* (:pid window) (:id window)))

(defn focused []
  (p/focused @*impl*))

(defn become-main [window]
  (p/become-main @*impl* (:pid window) (:id window)))

(defn set-frame [window frame]
  (p/set-frame @*impl* (:pid window) (:id window) frame))

(defn get-frame [window]
  (p/get-frame @*impl* (:pid window) (:id window)))

(defn set-minimized [window minimized?]
  (p/set-minimized @*impl* (:pid window) (:id window) minimized?))

(defn add-listener [event f]
  (p/add-listener @*impl* event f))

(defn remove-listener [event f]
  (p/remove-listener @*impl* event f))

