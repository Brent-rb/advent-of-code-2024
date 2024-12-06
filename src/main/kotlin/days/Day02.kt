package be.brentberghmans.advent2024.days

import be.brentberghmans.advent2024.extensions.filterEmpty
import be.brentberghmans.advent2024.extensions.some
import be.brentberghmans.advent2024.extensions.toIntList
import be.brentberghmans.advent2024.models.Day
import be.brentberghmans.advent2024.models.SortState
import kotlin.math.abs

class Day02: Day {

    /**
     * The high-level solution of A is pretty simple, transform the line to a list of integers
     * and then count the rows that are valid
     */
    override fun solveA(data: List<String>): String {
        val rows = data.map { line ->
            line.toIntList()
        }

        return rows.count { row ->
            isValidRow(row)
        }.toString()
    }

    /**
     * The high-level solution for B is a bit more complicated, but I decided to go for an easy approach
     * We still do the same as part A: convert the lines to a list of integers and check if the row is valid.
     * But if it is not, we make a list of copies of the row and try removing one number from each of them.
     * Then we check all the "permutations" to see if any one of them is valid. If so, we count the row as valid
     *
     * There are some optimisations in here that make it a bit faster to execute.
     * For instance, the check to see if the unmodified row is a valid row is actually not needed,
     * but it can save the cpu some work :)
     * I also made an extension function `some` on the List class, it uses `firstOrNull` so that we do an early exit
     * if we find a valid permutation.
     *
     * Initially I had extended the `isValidRow` function to try a different iteration of the input by removing
     * one of the numbers of the pair that is being examined, the idea being that one of these numbers are probably the issue
     * That's a good idea and optimisation in theory but in practice there's a lot of edge cases to get right.
     * For example, in the list [5, 4, 5, 6, 7, 8], the first pair (5, 4) is valid and is descending.
     * However, the next pair will have an issue [4, 5] because it is now ascending,
     * and the rest of the list ascending too, so we actually need to remove the first number in the list to solve this.
     * There is probably a way to handle all edge cases like this, and it would be the most performant solution.
     * But I had limited time, so I scrapped it and went to the permutation solution, which is simple and elegant.
     */
    override fun solveB(data: List<String>): String {
        val rows = data.filterEmpty().map { line ->
            line.toIntList()
        }

        return rows.count { row ->
            // If the row is valid by itself, nothing else to be done, easy!
            if (isValidRow(row)) {
                return@count true
            }

            // Generate all valid permutations
            // Which means, for each element in the row, make a copy of the row and remove that element
            // And check if any them produces a valid row
            row.indices.toList().some { index ->
                val permutation = row.toMutableList()
                permutation.removeAt(index)
                isValidRow(permutation)
            }
        }.toString()
    }

    /**
     * This is an example of the simplest solution as spoken about in solveB
     * It removes the `isValidRow(row)` check, which isn't strictly needed.
     * This is because if the unmodified row is valid, it means that the first permutation will also be valid
     */
    fun solveBSimple(data: List<String>): String {
        val rows = data.filterEmpty().map { line ->
            line.toIntList()
        }

        return rows.count { row ->
            // Generate all valid permutations
            // Which means, for each element in the row, make a copy of the row and remove that element
            // And check if any them produces a valid row
            row.indices.toList().some { index ->
                val permutation = row.toMutableList()
                permutation.removeAt(index)
                isValidRow(permutation)
            }
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