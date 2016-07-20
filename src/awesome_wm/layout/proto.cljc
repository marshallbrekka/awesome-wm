;; Protocol for all layouts
(ns awesome-wm.layout.proto)

(defprotocol ILayout
  ;; Given a workspace object, should return a list of
  ;; frame objects in order corrisponding to the list
  ;; of windows for the provided workspace.
  (arrange [this workspace screen-frame workarea-frame padding useless-gap]))
