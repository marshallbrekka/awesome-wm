(ns awesome-wm.api.hotkey.impl
  (:require [awesome-wm.api.hotkey.proto :refer [iHotkey]]
            [goog.object :as obj])
  (:refer-clojure :exclude [remove]))

;; (defrecord JSHotkey []
;;   iHotkey
;;   (add [_ k modifiers f]
;;     (.add (obj/get js/aw "hotkey") k (clj->js modifiers) f))
;;   (remove [_ k modifiers f]
;;     (.remove (obj/get js/aw "hotkey") k (clj->js modifiers) f)))

(defrecord PheonixHotkey []
  iHotkey
  (add [_ k modifiers f]
    (. js/Key on k (clj->js modifiers) f))
  (remove [_ k modifiers f] nil))


(defn construct []
  (PheonixHotkey.))
