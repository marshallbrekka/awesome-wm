(ns awesome-wm.api.applications.core
  (:require [awesome-wm.api.applications.impl :as impl]
            [awesome-wm.api.applications.proto :as p]))

(def ^:dynamic *impl* (delay (impl/construct)))

(defn applications []
  (p/applications @*impl*))

(defn activate [app]
  (p/activate @*impl* (:pid app)))


(defn add-listener [event f]
  (p/add-listener @*impl* event f))

(defn remove-listener [event f]
  (p/remove-listener @*impl* event f))
