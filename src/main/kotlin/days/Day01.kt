package be.brentberghmans.advent2024.days

import be.brentberghmans.advent2024.models.Day
import be.brentberghmans.advent2024.utils.ListUtils
import kotlin.math.abs

class Day01(): Day {
    override fun solveA(data: List<String>): String {
        val numberEntries = ListUtils.getVerticalLists(data, "   ") {
            it.toInt()
        }

        val leftList = numberEntries.map { it[0] }.sorted()
        val rightList = numberEntries.map { it[1] }.sorted()

        val differences = leftList.mapIndexed { index, entry ->
            abs(entry - rightList[index])
        }

        return differences.sum().toString()
    }

    override fun solveB(data: List<String>): String {
        val numberEntries = ListUtils.getVerticalLists(data, "   ") {
            it.toInt()
        }

        val leftList = numberEntries.map { it[0] }
        val rightList = numberEntries.map { it[1] }

        val rightListOccurrences = mutableMapOf<Int, Int>()
        rightList.forEach {
            rightListOccurrences[it] = (rightListOccurrences[it] ?: 0) + 1
        }

        val solution = leftList.sumOf {
            it * (rightListOccurrences[it] ?: 0)
        }

        return solution.toString()
    }
}