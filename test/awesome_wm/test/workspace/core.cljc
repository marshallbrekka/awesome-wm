(ns awesome-wm.test.workspace.core
  (:require [awesome-wm.workspace.core :as workspace]
            [clojure.test :refer [deftest is]]))

(def blank-workspace (workspace/make-workspace))

(deftest make-workspace
  (is (= {:windows []
          :focused nil
          :layout {:strategy :none}
          :full-screen false}
         blank-workspace)))

(deftest focus-window
  (is (= {:windows []
          :focused {:id "foo"}
          :layout {:strategy :none}
          :full-screen false}
         (workspace/focus-window blank-workspace {:id "foo"}))))

(deftest add-window
  (is (= {:windows [{:id "foo"}]
          :focused {:id "foo"}
          :layout {:strategy :none}
          :full-screen false}
         (workspace/add-window blank-workspace {:id "foo"})))

  (is (= {:windows [{:id "1"}
                    {:id "2"}
                    {:id "3"}]
          :focused {:id "3"}
          :layout {:strategy :none}
          :full-screen false}
         (-> blank-workspace
             (workspace/add-window {:id "1"})
             (workspace/add-window {:id "2"})
             (workspace/add-window {:id "3"})))))

(deftest remove-window
  (is (= {:windows []
          :focused nil
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "foo"}]
              :focused {:id "foo"}
              :layout {:strategy :none}
              :full-screen false}
             (workspace/remove-window {:id "foo"}))))

  (is (= {:windows [{:id "2"}
                    {:id "3"}]
          :focused {:id "2"}
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2"}
                        {:id "3"}]
              :focused {:id "1"}
              :layout {:strategy :none}
              :full-screen false}
             (workspace/remove-window {:id "1"}))))

  (is (= {:windows [{:id "1"}
                    {:id "3"}]
          :focused {:id "1"}
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2"}
                        {:id "3"}]
              :focused {:id "2"}
              :layout {:strategy :none}
              :full-screen false}
             (workspace/remove-window {:id "2"}))))

  (is (= {:windows [{:id "1"}
                    {:id "2"}]
          :focused {:id "2"}
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2"}
                        {:id "3"}]
              :focused {:id "2"}
              :layout {:strategy :none}
              :full-screen false}
             (workspace/remove-window {:id "3"})))))

(deftest rotate-focused-window-forward
  (is (= {:windows [{:id "2"}
                    {:id "1"}
                    {:id "3"}]
          :focused {:id "2"}
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2"}
                        {:id "3"}]
              :focused {:id "2"}
              :layout {:strategy :none}
              :full-screen false}
             (workspace/rotate-focused-window-forward))))

  (is (= {:windows [{:id "2"}
                    {:id "3"}
                    {:id "1"}]
          :focused {:id "1"}
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2"}
                        {:id "3"}]
              :focused {:id "1"}
              :layout {:strategy :none}
              :full-screen false}
             (workspace/rotate-focused-window-forward)))))

(deftest rotate-focused-window-backward
  (is (= {:windows [{:id "1"}
                    {:id "3"}
                    {:id "2"}]
          :focused {:id "2"}
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2"}
                        {:id "3"}]
              :focused {:id "2"}
              :layout {:strategy :none}
              :full-screen false}
             (workspace/rotate-focused-window-backward))))

  (is (= {:windows [{:id "3"}
                    {:id "1"}
                    {:id "2"}]
          :focused {:id "3"}
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2"}
                        {:id "3"}]
              :focused {:id "3"}
              :layout {:strategy :none}
              :full-screen false}
             (workspace/rotate-focused-window-backward)))))
