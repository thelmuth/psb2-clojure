[![Clojars Project](https://img.shields.io/clojars/v/net.clojars.schneau/psb2.svg)](https://clojars.org/net.clojars.schneau/psb2)

# PSB2 - Clojure Sampling Library

A Clojure library for fetching and sampling training and test data for experimenting with the program synthesis dataset PSB2. The library will automatically download datasets to the given location, and will cache them to avoid repeated downloads.

## Installation

Add the following to your `:dependencies`:

```clojure
[net.clojars.schneau/psb2 "1.1.0"]
```

Then, add the following to your namespace:

```clojure
(:require [psb2.core :as psb2])
```

## Usage

There is one core function and one constant available in this library.

The `fetch-examples` function downloads (if necessary) and samples training and test data for a specific problem in PSB2:

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

The `problems` constant is a list of problem names in PSB2:

```clojure
>>> psb2/problems
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

## Citation

If you use these datasets in a publication, please cite the paper *PSB2: The Second Program Synthesis Benchmark Suite* and include a link to this repository.

BibTeX entry for paper:

```bibtex
@InProceedings{Helmuth:2021:GECCO,
  author =	"Thomas Helmuth and Peter Kelly",
  title =	"{PSB2}: The Second Program Synthesis Benchmark Suite",
  booktitle =	"2021 Genetic and Evolutionary Computation Conference",
  series = {GECCO '21},
  year = 	"2021",
  isbn13 = {978-1-4503-8350-9},
  address = {Lille, France},
  size = {10 pages},
  doi = {10.1145/3449639.3459285},
  publisher = {ACM},
  publisher_address = {New York, NY, USA},
  month = {10-14} # jul,
  doi-url = {https://doi.org/10.1145/3449639.3459285},
  URL = {https://dl.acm.org/doi/10.1145/3449639.3459285},
}
```

## License

Copyright © 2021 Thomas Helmuth

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.
