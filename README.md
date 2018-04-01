# Sequence Set

A data structure for allocating new unused numbers quickly and that optimizes for sequential integer runs (ie, 1, 2, 3, 4, ...)

## Motivation

Often I see myself trying to get the next number of something; an object, a message, etc., and often I want to support both randomly allocated numbers (ie, by humans) as well as computer generated consecutive numbers. So I implemented this structure.

## Description

The data structure is basically a binary self-balancing tree where the nodes are non-overlapping integer intervals. Nodes with intervals that "touch" eachother are merged into one node.

## Analysis

| Operation                                   | Time complexity | Space complexity |
------------------------------------------------------------------------------------
| Insert (consecutive numbers)                | O(1)            | O(1)             |
| Insert (random non-consecutive numbers)     | O(log n)        | O(n)             |
| Allocate (get next + insert)                | O(1)            | O(1)             |
| Lookup                                      | O(log n)        |                  |
| Fetch min/max                               | O(log n)        |                  |
------------------------------------------------------------------------------------

## TODO

* Investigate why the height of the tree doesn't completely follow ceil(log2(N)). It's probably due to the balancing; the number of cases could be extended to 4 like in AVL