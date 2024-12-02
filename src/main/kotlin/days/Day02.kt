package be.brentberghmans.advent2024.days

import be.brentberghmans.advent2024.extensions.some
import be.brentberghmans.advent2024.extensions.toIntList
import be.brentberghmans.advent2024.models.Day
import be.brentberghmans.advent2024.models.SortState
import kotlin.math.abs

class Day02: Day {
    override fun solveA(data: List<String>): String {
        val rows = data.map { line ->
            line.toIntList()
        }

        return rows.count { row ->
            isValidRow(row)
        }.toString()
    }
    override fun solveB(data: List<String>): String {
        val rows = data.map { line ->
            line.toIntList()
        }

        return rows.count { row ->
            // If the row is valid by itself, nothing else to be done, easy!
            if (isValidRow(row)) {
                return@count true
            }

            // Generate all valid permutations
            // Which means, for each element in the row, make a copy of the row and remove that element
            val allPermutations = row.indices.map { index ->
                val subRow = row.toMutableList()
                subRow.removeAt(index)
                subRow
            }

            // Try all permutations with an early exit
            return@count allPermutations.some { isValidRow(it) }
        }.toString()
    }

    private fun isValidRow(row: List<Int>): Boolean {
        // If it's an empty list or only 1 item it is valid by default
        if (row.size < 2) {
            return true
        }

        // Determine if the row is ascending or descending
        val sort = SortState.from(row[0], row[1])
        // If the values were equal this is not allowed
        if (sort == SortState.Equal) {
            return false
        }

        for(i in 1 until row.size) {
            val a = row[i - 1]
            val b = row[i]

            // Compare the numbers and check if consistent with direction
            if (a < b && sort == SortState.Descending)  {
                return false
            }
            if  (a > b && sort == SortState.Ascending) {
                return false
            }

            // If the order is correct, check if the difference between them is allowed
            // No need for a sign check, should be covered by the sort state
            if (abs(a - b) !in 1 .. 3) {
                return false
            }
        }

        // The row has passed all trials, it has been blessed by the machine spirit
        return true
    }
}