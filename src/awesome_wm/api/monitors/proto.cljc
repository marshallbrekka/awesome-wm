(ns awesome-wm.api.monitors.proto)

(defprotocol iMonitors
  (monitors [this])
  (add-listener [this f])
  (remove-listener [this f]))
