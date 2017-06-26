(ns awesome-wm.api.hotkey.impl
  (:require [awesome-wm.api.hotkey.proto :refer [iHotkey]])
  (:refer-clojure :exclude [remove]))

(defrecord TestHotkey []
  iHotkey
  (add [_ k modifiers f]
    nil)
  (remove [_ k modifiers f]
    nil))

(defn construct []
  (TestHotkey.))
