(ns leiningen.ivy
  (:require [vine.core :as vine]
            [cemerick.pomegranate :as pomegranate]
            [leiningen.core.classpath]
            [leiningen.help :refer [help-for subtask-help-for]]
            [clojure.pprint :refer [pprint]]
            [robert.hooke]))

(def ivy-instance
  (memoize vine/ivy-instance))

(def resolve-report
  (memoize
   (fn [project]
     (-> project
         ivy-instance
         (vine/ivy-resolve project)))))

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
  ;; Remove prep-tasks as it changes and prevents memoization.
  ;; Looking at the lein source, I'm thinking their memoize isn't
  ;; catching this.
  (let [project (dissoc project :prep-tasks)
        files (vine/report-files (resolve-report project) dependencies-key)]
    (doseq [file files]
      (if add-classpath?
        (pomegranate/add-classpath file)))
    ;; They changed extract-native-deps to depend on aether.
    ;; (leiningen.core.classpath/extract-native-deps files native-path)
    (set files)))

(def ivy-resolve-dependencies-memoized
  (memoize ivy-resolve-dependencies))

(defn resolve-hook [f & args]
  (apply ivy-resolve-dependencies-memoized args))

(defn hooks []
  (robert.hooke/add-hook #'leiningen.core.classpath/resolve-dependencies
                         resolve-hook))

(defn activate []
  (hooks))

(defn publish
  "Publish this project to an ivy repository."
  [project & args]
  (apply vine/ivy-publish (ivy-instance project) (resolve-report project) args))

(defn- nary? [v n]
  (some #{n} (map count (:arglists (meta v)))))

(defn ivy
  "Ivy related tasks."
  {:help-arglists '([publish])
   :subtasks [#'publish]}
  ([project]
     (println (if (nary? #'help-for 2)
                (help-for project "ivy")
                (help-for "ivy"))))
  ([project subtask & args]
     (case subtask
       "publish" (apply publish project args)
       (println "Subtask" (str \" subtask \") "not found."
                (subtask-help-for *ns* #'ivy)))))

