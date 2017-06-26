(ns awesome-wm.model.internal.util)

(defn index-of [vect item & [equality-fn]]
  (let [total (count vect)
        equality-fn (or equality-fn =)]
    (loop [idx 0]
      (when (not= idx total)
        (if (equality-fn (nth vect idx) item)
          idx
          (recur (inc idx)))))))


