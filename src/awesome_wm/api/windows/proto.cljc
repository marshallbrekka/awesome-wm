(ns awesome-wm.api.windows.proto)

(defprotocol iWindows
  (windows [this])
  (close [this pid id])
  (focused [this])
  (become-main [this pid id])
  (set-frame [this win frame])
  (get-frame [this win])
  (set-minimized [this pid id minimize?])
  (add-listener [this event f])
  (remove-listener [this event f]))
