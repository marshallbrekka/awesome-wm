(ns awesome-wm.api.monitors.core
  (:require [awesome-wm.api.monitors.impl :as impl]
            [awesome-wm.api.monitors.proto :as p]))

(def ^:dynamic *impl* (delay (impl/construct)))

(defn monitors []
  (p/monitors @*impl*))

(defn add-listener [f]
  (p/add-listener @*impl* f))

(defn remove-listener [f]
  (p/remove-listener @*impl* f))
