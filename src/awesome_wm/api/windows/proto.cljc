(ns awesome-wm.api.windows.proto)

(defprotocol iWindows
  (get [this])
  (close [this pid id])
  (focused [this])
  (become-main [this pid id])
  (set-frame [this pid id frame])
  (get-frame [this pid id])
  (set-minimized [this pid id minimize?])
  (add-listener [this event f])
  (remove-listener [this event f]))
