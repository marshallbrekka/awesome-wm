(ns awesome-wm.util.geometry)

(defn overlap [a-start a-length b-start b-length]
  (let [a-end (+ a-start a-length)
        b-end (+ b-start b-length)]
    (when (and (> a-end b-start)
               (< a-start b-end))
      (let [start (max a-start b-start)]
        {:length (- (min a-end b-end) start)
         :start  start}))))

(defn intersection-rect [rect-a rect-b]
  (let [x-overlap (overlap (:x rect-a)
                           (:width rect-a)
                           (:x rect-b)
                           (:width rect-b))
        y-overlap (overlap (:y rect-a)
                           (:height rect-a)
                           (:y rect-b)
                           (:height rect-b))]
    (when (and x-overlap y-overlap)
      {:x (:start x-overlap)
       :y (:start y-overlap)
       :width  (:length x-overlap)
       :height (:length y-overlap)})))
