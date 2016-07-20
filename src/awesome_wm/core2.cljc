(ns awesome-wm.core2
  (:require [awesome-wm.workspace.core :as workspace]
            [awesome-wm.api.windows.core :as windows]
            [awesome-wm.util.log :as log]))

(defn listener
  [world event & [event-details]]
  (log/log "LISTENER" world event event-details))

(defn start []
  (let [world (atom {:workspace (workspace/make-workspace)})
        listener-fn (partial listener world)]
    (doseq [event ["created" "destroyed" "focused"]]
      (log/log "adding listener" event)
      (windows/add-listener event listener-fn))))
