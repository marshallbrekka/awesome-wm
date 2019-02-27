(ns awesome-wm.api.cursor.proto)

(defprotocol iCursor
  (get-position [this])
  (set-position [this point])
  (click [this point]))
