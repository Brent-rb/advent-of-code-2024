package be.brentberghmans.advent2024.days

import be.brentberghmans.advent2024.extensions.filterEmpty
import be.brentberghmans.advent2024.extensions.toIntList
import be.brentberghmans.advent2024.models.Day
import kotlin.math.abs

class Day01(): Day {
    /**
     * Given that this is the first problem it's quite simple to solve.
     * 1. Parse the vertical lists
     * 2. Sort them (direction doesn't matter)
     * 3. Calculate the sum of the differences
     */
    override fun solveA(data: List<String>): String {
        val rows = data.filterEmpty().map { it.toIntList("   ") }
        val column0 = rows.map { it[0] }.sorted()
        val column1 = rows.map { it[1] }.sorted()
        val differences = rows.indices.map {
            abs(column0[it] - column1[it])
        }

        return differences.sum().toString()
    }

    /**
     * A slightly more difficult problem but luckily easily solvable by using my favorite data structure: a map
     * 1. Parse the vertical lists
     * 2. For the second list, calculate the occurrences of each number and construct a map
     * 3. Iterate over the first list and multiply the number by the amount of occurrences in the second list
     * 4. Sum the result of 3
     */
    override fun solveB(data: List<String>): String {
        val rows = data.filterEmpty().map { it.toIntList("   ") }
        val column0 = rows.map { it[0] }
        val column1 = rows.map { it[1] }
        val occurrences = column1.groupingBy { it }.eachCount()

        val solution = column0.sumOf {
            it * (occurrences[it] ?: 0)
        }

        return solution.toString()
    }
}