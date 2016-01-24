(ns awesome-wm.api.hotkey.proto)

(defprotocol iHotkey
  (add [this k modifiers f])
  (remove [this k modifiers f]))
