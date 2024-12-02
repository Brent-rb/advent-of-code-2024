package be.brentberghmans.advent2024.days

import be.brentberghmans.advent2024.extensions.toIntList
import be.brentberghmans.advent2024.models.Day
import kotlin.math.abs
import kotlin.math.cos

class Day01(): Day {
    override fun solveA(data: List<String>): String {
        val rows = data.map { it.toIntList("   ") }
        val column0 = rows.map { it[0] }.sorted()
        val column1 = rows.map { it[1] }.sorted()
        val differences = rows.indices.map {
            abs(column0[it] - column1[it])
        }

        return differences.sum().toString()
    }

    override fun solveB(data: List<String>): String {
        val rows = data.map { it.toIntList("   ") }
        val column0 = rows.map { it[0] }
        val column1 = rows.map { it[1] }
        val occurrences = column1.groupingBy { it }.eachCount()

        val solution = column0.sumOf {
            it * (occurrences[it] ?: 0)
        }

        return solution.toString()
    }
}