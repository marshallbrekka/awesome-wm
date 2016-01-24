(ns awesome-wm.model.internal.util)

(defn index-of [vect item]
  (let [total (count vect)]
    (loop [idx 0]
      (when (not= idx total)
        (if (= (nth vect idx) item)
          idx
          (recur (inc idx)))))))


