This repo contains my solutions to the advent of code 2024, written in Kotlin.

### Project Setup

Over the past few years, taking part in the AoC, I've always had frustrations with the boilerplate to setup the solution for a new day. So this year I've taken some time to create an automated way of handling this.
I'm quite happy with the result and that's why I want to talk about it. :)

The core of the automation is the `DayWalker` class. It will check which files are present in the `io` folder and then, using reflection, it will load the correct day and call its `solveA` or `solveB` functions with either the example data or the actual data, depending on the file.

Here is an example of files:

```
io/
  days-01-a.txt           // Will execute Day01.solveA with the file input
  days-01-a.example.txt   // Will execute Day01.solveA with the file input
  days-01-b.txt           // Will execute Day01.solveB with the file input
  days-01-b.example.txt   // Will execute Day01.solveB with the file input
```
