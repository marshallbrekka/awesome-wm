(ns awesome-wm.util.log)

#?(:cljs
   (defn log [arg & args]
     (. js/Phoenix log arg))

   :clj
   (defn log [& args]
     (apply println args)))
