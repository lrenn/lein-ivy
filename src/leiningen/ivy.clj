(ns leiningen.ivy
  (:require [vine.core :as vine]
            [cemerick.pomegranate :as pomegranate]
            [leiningen.core.classpath]
            [robert.hooke]))

(defn ivy-resolve-dependencies
  "Simply delegate regular dependencies to vine/ivy. This will
  ensure they are downloaded into ~/.ivy2/cache and that native
  deps have been extracted to :native-path.  If :add-classpath? is
  logically true, will add the resolved dependencies to Leiningen's
  classpath.

   Returns a set of the dependencies' files."
  [dependencies-key {:keys [repositories native-path] :as project}
   & {:keys [add-classpath?]}]
  {:pre [(every? vector? (project dependencies-key))]}
  (let [files (vine/resolve-dependencies-xml project dependencies-key)]
    (doseq [file files]
      (if add-classpath?
        (pomegranate/add-classpath file)))
    (leiningen.core.classpath/extract-native-deps files native-path)
    (set files)))

(defn resolve-hook [f & args]
  (apply ivy-resolve-dependencies args))

(defn hooks []
  (robert.hooke/add-hook #'leiningen.core.classpath/resolve-dependencies
                         resolve-hook))

(defn activate []
  (hooks))
