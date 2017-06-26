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

(deftest merge-workspace
  (is (= {:windows [1 2 3]
          :focused 1
          :layout {:strategy :none}
          :full-screen false}
         (workspace/merge-workspace {:windows [1]
                                     :focused 1
                                     :layout {:strategy :none}
                                     :full-screen false}
                                    {:windows [2 3]
                                     :focused 2
                                     :layout {:strategy :other}
                                     :full-screen true}))))

(deftest contains-window?
  (is (= true
         (workspace/contains-window?
          {:windows [{:id 1} {:id 2} {:id 3}]
           :focused 1
           :layout {:strategy :none}
           :full-screen false}
          {:id 2})))

  (is (= false
         (workspace/contains-window?
          {:windows [{:id 1} {:id 2} {:id 3}]
           :focused 1
           :layout {:strategy :none}
           :full-screen false}
          {:id 4}))))

(deftest focus-window
  ;; Sets the focused window id to 1
  (is (= {:windows [{:id 1}]
          :focused 1
          :layout {:strategy :none}
          :full-screen false}
         (workspace/focus-window {:windows [{:id 1}]
                                  :focused nil
                                  :layout {:strategy :none}
                                  :full-screen false}
                                 {:id 1})))

  ;; The window being focused is not present in the workspace,
  ;; therefore it is a noop.
  (is (= {:windows [{:id 1}]
          :focused 1
          :layout {:strategy :none}
          :full-screen false}
         (workspace/focus-window {:windows [{:id 1}]
                                  :focused 1
                                  :layout {:strategy :none}
                                  :full-screen false}
                                 {:id 2}))))

(deftest focused-window
  ;; There are no windows in the workspace
  (is (nil? (workspace/focused-window blank-workspace)))

  (is (= {:id "foobar" :pid "1234"}
         (workspace/focused-window (assoc blank-workspace
                                          :windows [{:id "foobar" :pid "1234"}]
                                          :focused "foobar")))))

(deftest add-window
  (is (= {:windows [{:id "foo"}]
          :focused "foo"
          :layout {:strategy :none}
          :full-screen false}
         (workspace/add-window blank-workspace {:id "foo"})))

  (is (= {:windows [{:id "1"}
                    {:id "2"}
                    {:id "3"}]
          :focused "3"
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
              :focused "foo"
              :layout {:strategy :none}
              :full-screen false}
             (workspace/remove-window {:id "foo"}))))

  (is (= {:windows [{:id "2"}
                    {:id "3"}]
          :focused "2"
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2"}
                        {:id "3"}]
              :focused "1"
              :layout {:strategy :none}
              :full-screen false}
             (workspace/remove-window {:id "1"}))))

  (is (= {:windows [{:id "1"}
                    {:id "3"}]
          :focused "1"
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2"}
                        {:id "3"}]
              :focused "2"
              :layout {:strategy :none}
              :full-screen false}
             (workspace/remove-window {:id "2"}))))

  (is (= {:windows [{:id "1"}
                    {:id "2"}]
          :focused "2"
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2"}
                        {:id "3"}]
              :focused "2"
              :layout {:strategy :none}
              :full-screen false}
             (workspace/remove-window {:id "3"})))))

(deftest update-window
  ;; Don't update the window if it didn't exist in the first place.
  (is (= {:windows []
          :focused nil
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows []
              :focused nil
              :layout {:strategy :none}
              :full-screen false}
             (workspace/update-window {:id "foo"}))))

  ;; Updates the window in the list
  (is (= {:windows [{:id "1"}
                    {:id "2" :pid 123}
                    {:id "3"}]
          :focused "2"
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2" :pid 456 :bogus "foo"}
                        {:id "3"}]
              :focused "2"
              :layout {:strategy :none}
              :full-screen false}
             (workspace/update-window {:id "2" :pid 123})))))

(deftest rotate-focused-window-forward
  (is (= {:windows [{:id "2"}
                    {:id "1"}
                    {:id "3"}]
          :focused "2"
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2"}
                        {:id "3"}]
              :focused "2"
              :layout {:strategy :none}
              :full-screen false}
             (workspace/rotate-focused-window-forward))))

  (is (= {:windows [{:id "2"}
                    {:id "3"}
                    {:id "1"}]
          :focused "1"
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2"}
                        {:id "3"}]
              :focused "1"
              :layout {:strategy :none}
              :full-screen false}
             (workspace/rotate-focused-window-forward)))))

(deftest rotate-focused-window-backward
  (is (= {:windows [{:id "1"}
                    {:id "3"}
                    {:id "2"}]
          :focused "2"
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2"}
                        {:id "3"}]
              :focused "2"
              :layout {:strategy :none}
              :full-screen false}
             (workspace/rotate-focused-window-backward))))

  (is (= {:windows [{:id "3"}
                    {:id "1"}
                    {:id "2"}]
          :focused "3"
          :layout {:strategy :none}
          :full-screen false}
         (-> {:windows [{:id "1"}
                        {:id "2"}
                        {:id "3"}]
              :focused "3"
              :layout {:strategy :none}
              :full-screen false}
             (workspace/rotate-focused-window-backward)))))
