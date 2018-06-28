# Sequence Set

A data structure for allocating new unused numbers quickly and that optimizes for sequential integer runs (ie, 1, 2, 3, 4, ...)

```java
final SeqSet s = new SeqSet();

// Insert first run of integers
s.insert(1);
s.insert(2);
s.insert(3);

// Second run
s.insert(5);
s.insert(6);

// Check the nodes in the tree
s.size(); // Returns 2

// Take the next free value, O(1)
s.take(); // Returns 4

// Check that the nodes have been merged
s.size(); // Returns 1
```

## Motivation

Often I see myself trying to get the next number of something; an object, a message, etc., and often I want to support both randomly allocated numbers (ie, by humans) as well as computer generated consecutive numbers. So I implemented this structure.

## Description

The data structure is basically a binary self-balancing tree where the nodes are non-overlapping integer intervals. Nodes with intervals that "touch" eachother are merged into one node.

## Analysis

Operation                                   | Worst case time | Space complexity
--------------------------------------------|-----------------|-----------------
Insert (consecutive numbers)                | O(1)            | O(1)
Insert (random non-consecutive numbers)     | O(log n)        | O(n)
Allocate (get next + insert)                | O(1)            | O(1)
Lookup                                      | O(log n)        |
Fetch min/max                               | O(log n)        |


## TODO

* Investigate why the height of the tree doesn't completely follow ceil(log2(N)). It's probably due to the balancing; the number of cases could be extended to 4 like in AVL

## Copyright

Copyright 2018, Iostream Solutions AB.
Released under the terms of the MIT license (see `LICENSE`).

Created by
[Peter Backman](http://www.iostream.cc/).