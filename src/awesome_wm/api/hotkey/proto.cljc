(ns awesome-wm.api.hotkey.proto
  (:refer-clojure :exclude [remove]))

(defprotocol iHotkey
  (add [this k modifiers f])
  (remove [this k modifiers f]))
