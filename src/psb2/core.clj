(ns psb2.core
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]))

(def problems '("basement"
                "bouncing-balls"
                "bowling"
                "camel-case"
                "coin-sums"
                "cut-vector"
                "dice-game"
                "find-pair"
                "fizz-buzz"
                "fuel-cost"
                "gcd"
                "indices-of-substring"
                "leaders"
                "luhn"
                "mastermind"
                "middle-character"
                "paired-digits"
                "shopping-list"
                "snow-day"
                "solve-boolean"
                "spin-words"
                "square-digits"
                "substitution-cipher"
                "twitter"
                "vector-distance"))

(def psb1-problems '("checksum"
                     "collatz-numbers"
                     "compare-string-lengths"
                     "count-odds"
                     "digits"
                     "double-letters"
                     "even-squares"
                     "for-loop-index"
                     "grade"
                     "last-index-of-zero"
                     "median"
                     "mirror-image"
                     "negative-to-zero"
                     "number-io"
                     "pig-latin"
                     "replace-space-with-newline"
                     "scrabble-score"
                     "small-or-large"
                     "smallest"
                     "string-differences"
                     "string-lengths-backwards"
                     "sum-of-squares"
                     "super-anagrams"
                     "syllables"
                     "vector-average"
                     "vectors-summed"
                     "wallis-pi"
                     "word-stats"
                     "x-word-lines"))

(defn save-file-from-uri
  "Saves a file given a particular URI.
   Taken from here: https://stackoverflow.com/a/19297746/2023312"
  [uri file]
  (with-open [in (io/input-stream uri)
              out (io/output-stream file)]
    (io/copy in out)))

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

(defn fetch-and-possibly-cache-data
  "Helper function for fetch-examples that does the following for edge or random dataset:
    1. Checks if EDN file for dataset is already downloaded.
    2. If not, downloads the dataset file to the specified location.
    3. Loads and returns list of the data from the dataset file."
  [datasets-directory problem-name edge-or-random]
  (let [problem-name (name problem-name) ; Make this work with keywords
        directory (io/file datasets-directory "datasets" problem-name)
        file (io/file directory (str problem-name "-" edge-or-random ".edn"))
        psbN (cond
               (some #{problem-name} problems) "PSB2"
               (some #{problem-name} psb1-problems) "PSB1"
               :else (throw (str "Unrecognized problem: " problem-name)))]
    ; Make directory if it doesn't exist
    (when (not (.exists directory))
      (.mkdirs directory))
    ; Download file from S3 if it doesn't exist
    (when (not (.exists file))
      (let [uri (format "https://psb2-datasets.s3.amazonaws.com/%s/datasets/%s/%s-%s.edn"
                        psbN problem-name problem-name edge-or-random)]
        (save-file-from-uri uri file)))
    ; Load file
    (load-edn-lines file)))

(defn sample
  "Returns a random sample of given collection of size sample-size"
  [coll sample-size]
  (repeatedly sample-size #(rand-nth coll)))

(defn fetch-examples
  "Downloads, fetches, and returns training and test data from a PSB2 problem.
   Caches downloaded datasets in `datasets_directory` to avoid multiple downloads.
   Returns a map of the form {:train training-examples :test testing-examples}
   where training-examples and testing-examples are lists of training and test
   data. The elements of these lists are maps of the form:
   {:input1 first-input :input2 second-input ... :output1 first-output ...}
   The training examples will include all hard-coded edge cases included in the suite,
   along with enough random cases to include `n-train` cases.
   Note that this function downloads and loads large datasets and can
   be slow, up to 2 minutes.
   Parameters:
     `datasets-directory` - Location to download the PSB2 datasets.
     `problem-name` - Name of the PSB2 problem, lowercase and seperated by dashes.
         - Ex: indices-of-substring
     `n-train` - Number of training cases to return
     `n-test` - Number of test cases to return"
  [datasets-directory problem-name n-train n-test]
  (let [edge (fetch-and-possibly-cache-data datasets-directory problem-name "edge")
        random (fetch-and-possibly-cache-data datasets-directory problem-name "random")
        train (if (< n-train (count edge))
                (sample edge n-train)
                (concat edge
                        (sample random
                                (- n-train (count edge)))))
        test (sample random n-test)]
    {:train train
     :test test}))

(defn get-problem-names
  "Returns a list of strings of the problem names in PSB2."
  []
  problems)
