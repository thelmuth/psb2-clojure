(ns psb2.core
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defn load-edn-lines
  "Load edn from an io/reader source (filename or io/resource).
   Expects file to have multiple lines of EDN."
  [source]
  (try
    (with-open [r (java.io.PushbackReader. (io/reader source))]
      (loop [examples []]
        (let [next-line (edn/read {:eof nil} r)]
          (if next-line
            (recur (conj examples next-line))
            examples))))
    (catch java.io.IOException e
      (printf "Couldn't open '%s': %s\n" source (.getMessage e)))
    (catch RuntimeException e
      (printf "Error parsing edn file '%s': %s\n" source (.getMessage e)))))

(defn sample
  "Returns a random sample of given collection of size sample-size"
  [coll sample-size]
  (repeatedly sample-size #(rand-nth coll)))

(defn fetch-examples
  "Fetches and returns training and test data from a PSB2 problem.
   Returns a map of the form {:train training-examples :test testing-examples}
   where training-examples and testing-examples are lists of training and test
   data.
   The training examples will include all hard-coded edge cases included in the suite,
   along with enough random cases to include `n-train` cases.
   Note that this function loads large datasets and can be slow, 30-120 seconds.
   Parameters:
     `datasets-directory` - Location of the PSB2 datasets as downloaded from https://zenodo.org/record/4678739
     `problem-name` - Name of the PSB2 problem, lowercase and seperated by dashes.
         - Ex: indices-of-substring
     `n-train` - Number of training cases to return
     `n-test` - Number of test cases to return"
  [datasets-directory problem-name n-train n-test]
  (let [filename-base (str datasets-directory
                           "/datasets/"
                           problem-name
                           "/"
                           problem-name)
        edge (load-edn-lines (str filename-base "-edge.edn"))
        random (load-edn-lines (str filename-base "-random.edn"))
        train (if (< n-train (count edge))
                (sample edge n-train)
                (concat edge
                        (sample random
                                (- n-train (count edge)))))
        test (sample random n-test)]
    {:train train
     :test test}))

(defn get-problem-names
  "Returns a list of strings of the problem names in PSB2.
   Takes the location of the PSB2 datasets as parameter `datasets-directory`.
   Bases this off of the directory names in
   `datasets-directory`/datasets"
  [datasets-directory]
  (map #(.getName %)
       (filter #(.isDirectory %)
               (.listFiles (io/file (str datasets-directory
                                         "/datasets"))))))
