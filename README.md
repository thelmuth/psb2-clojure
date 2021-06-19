# PSB2 - Clojure Sampling Library

A Clojure library for fetching and sampling training and test data for experimenting with the program synthesis dataset PSB2. In order to use this library, you need to have [the PSB2 datasets downloaded from Zenodo](https://zenodo.org/record/4678739).

## Installation

Add the following to your `:dependencies`:

```clojure
[psb2 "1.0.0"]
```

## Usage

There are two functions available in this library; both require the location of the PSB2 datasets in order to run.

The `fetch-examples` function can be used to collect and sample data for a specific problem in PSB2:

```clojure
>>> (psb2/fetch-examples "path/to/PSB2/datasets/" "fizz-buzz" 200 2000)
{:train
 ({:input1 1, :output1 "1"}
  {:input1 2, :output1 "2"}
  {:input1 3, :output1 "Fizz"}
  ...
  {:input1 50000, :output1 "Buzz"}
  {:input1 765678, :output1 "Fizz"}),
 :test
 ({:input1 83650, :output1 "Buzz"}
  {:input1 223565, :output1 "Buzz"}
  ...
  {:input1 784600, :output1 "Buzz"}
  {:input1 407367, :output1 "Fizz"})}
```

The `get-problem-names` function returns a list of problem names in PSB2:

```clojure
>>> (psb2/get-problem-names "path/to/PSB2/datasets/")
("basement"
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
 "vector-distance")
```

## License

Copyright © 2021 Thomas Helmuth

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.
