(ns awesome-wm.api.cursor.core
  (:require [awesome-wm.api.cursor.impl :as impl]
            [awesome-wm.api.cursor.proto :as p]))

(def ^:dynamic *impl* (delay (impl/construct)))

(defn get-position []
  (p/get-position @*impl*))

(defn set-position [point]
  (p/set-position @*impl* point))

(defn click [point]
  (p/click @*impl* point))
