(ns awesome-wm.api.applications.impl
  (:require [awesome-wm.api.events :as events]
            [awesome-wm.api.applications.proto :refer [iApplications]]
            [goog.object :as obj]))

(defn ->event-name [event]
  (str "aw.applications." event))

(defrecord JSApplications []
  iApplications
  (applications [_])
  ;; (-> (.applications (obj/get js/aw "application"))
  ;;     (js->clj :keywordize-keys true)))
  (activate [_ pid])
  ;; (.activate (obj/get js/aw "application") pid))
  (add-listener [_ event f]
    (events/add-event-listener (->event-name event) f))
  (remove-listener [_ event f]
    (events/remove-event-listener (->event-name event) f)))

(defn construct []
  (JSApplications.))
