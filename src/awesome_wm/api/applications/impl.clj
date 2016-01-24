(ns awesome-wm.api.applications.impl
  (:require [awesome-wm.api.applications.proto :refer [iApplications]]))

(def listeners (atom {}))
(def applications (atom [{:pid 1} {:pid 10} {:pid 25}]))

(defrecord TestApplications [listeners applications]
  iApplications
  (get [_]
    @applications)
  (activate [_ pid]
    nil)
  (add-listener [_ event f]
    nil)
  (remove-listener [_ event f]
    nil))

(defn construct []
  (TestApplications. listeners applications))
