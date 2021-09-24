# BCNF Decomposition Algorithm

This java program takes in a set of attributes and a set of functional dependencies as input
through STDIN and outputs the tables after BCNF decomposition

## Sample Input
- Relation Attributes
    - {A,B,C,D,E}
- Functional Dependencies
    - A,B->C
    - C->B,D
    - E->C

## Compile and Run
The only main method is in Decompose.java:
- Navigate into project directory
    - `cd bcnf-decomposition`
- Compile
    - `javac *.java`
- Run
    - `java Decompose`
