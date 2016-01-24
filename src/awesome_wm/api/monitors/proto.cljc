(ns awesome-wm.api.monitors.proto)

(defprotocol iMonitors
  (get [this])
  (add-listener [this f])
  (remove-listener [this f]))
