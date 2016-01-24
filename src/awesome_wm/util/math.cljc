(ns awesome-wm.util.math)

(defn floor [n]
  #?(:clj
     (Math/floor n)
     :cljs
     (.floor js/Math n)))
