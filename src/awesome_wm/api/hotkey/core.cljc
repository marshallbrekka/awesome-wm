(ns awesome-wm.api.hotkey.core
  (:require [awesome-wm.api.hotkey.impl :as impl]
            [awesome-wm.api.hotkey.proto :as p])
  (:refer-clojure :exclude [remove]))

#?(:cljs (.log js/console "hk.core"))
(def ^:dynamic *impl* (delay (impl/construct)))

(defn add [k modifiers f]
  (p/add @*impl* k modifiers f))

(defn remove [k modifiers f]
  (p/remove @*impl* k modifiers f))
