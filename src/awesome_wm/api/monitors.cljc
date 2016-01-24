(ns awesome-wm.api.monitors
  #?(:cljs (:require [awesome-wm.api.events :as events])))




#?(:cljs
   (defrecord JSMonitors []
     iMonitors
     (get [_]
       (-> (.monitors (obj/get js/aw "monitors"))
           (js->cljs :keywordize-keys)))
     (add-listener [_ f]
       (events/add-event-listener "aw.monitors.layoutChange" f))
     (remove-listener [_ f]
       (events/remove-event-listener "aw.monitors.layoutChange" f)))

   :clj
   (defrecord TestMonitors [listeners monitors]
     iMonitors
     (get [_]
       @monitors)
     (add-listener [_ f]
       (swap! listeners conj f))
     (remove-listener [_ f]
       (swap! listeners disj f))))




[{:id 1 :frame {:x 0 :y 0 :width 1000 :height 700}}
 {:id 2 :frame {:x -800 :y 10 :width 800 :height 7000}}]
