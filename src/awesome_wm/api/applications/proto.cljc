(ns awesome-wm.api.applications.proto)

(defprotocol iApplications
  (get [this])
  (activate [this pid])
  (add-listener [this event f])
  (remove-listener [this event f]))
