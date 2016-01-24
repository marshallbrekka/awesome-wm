(ns awesome-wm.util.log)

#?(:cljs
   (defn log [arg & args]
     (.log js/console arg args))
   :clj
   (defn log [& args]
     (apply println args)))
