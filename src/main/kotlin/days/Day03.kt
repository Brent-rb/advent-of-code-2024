package be.brentberghmans.advent2024.days

import be.brentberghmans.advent2024.models.Day

class Day03: Day {
    /**
     * This solution uses a simple regex to find all the mul(x,y) parts in the input
     * The match result will have 3 values:
     *   0 - the whole string,
     *   1 - the first number (x)
     *   2 - the second number (y)
     * It take x and y and converts them to an int and subsequently multiplies them.
     *
     * First we want to get a list of all matches, this can be done with regex.findAll
     * This will only work for one line however, as the input has multiple lines we can iterate over them
     * and then group the resulting lists by using flatMap
     *
     * Second and finally, we can use these matches to calculate the result of the mul function.
     * We then need to take the sum of all these results.
     * Luckily the sumOf function allows us to these two things easily with the sumOf function.
     */
    override fun solveA(data: List<String>): String {
        val regex = Regex("mul\\((\\d+),(\\d+)\\)")

        // Map the multiple lines to a flat list of match results
        val sumOfMultiples = data.flatMap {
            regex.findAll(it)
        // Calculate the sum of the multiplication
        }.sumOf {
            it.groupValues[1].toInt() * it.groupValues[2].toInt()
        }

        return sumOfMultiples.toString()
    }

    /**
     * The solution for part B is an extension to the solution of part A.
     * We still transform the input to a list of match results and take the sum of the multiples.
     * However, unlike part A we need to filter the matches,
     * so they only include the mul's that are within an enabled section.
     *
     * If you look back at part A, the first value of match result is the whole match.
     * In the case of part A, this was always `mul(x,y)`
     * combine that with the fact that the matches come in the order in which they appeared in the input, it gave me an idea
     *
     * We can extend the regex to include the `do()` and `don't()` functions.
     * This was achieved by adding `do()|don't()|` in front of the regex of part A.
     *
     * So now we have a list of [mul(x,y), do(), mul(x,y), don't(), ...] which has all the "junk" removed.
     * We can add a filter to this list so the result only contains valid `mul(x,y)` functions.
     * The result of a filter operation will be a list of items that pass the filter.
     *
     * In this case the filter is a whether the `mul` function is enabled or not.
     * For this, we add a variable `isEnabled` and change its value depending on if we encounter `do` or `don't`
     * If we encounter a `do` or `don't` we return false in the filter, so they aren't included in the result either.
     *
     * After that we have a list of `mul(x,y)` and we can do the same thing as part A: calculate the sum of the multiplications
     */
    override fun solveB(data: List<String>): String {
        val regex = Regex("do\\(\\)|don't\\(\\)|mul\\((\\d+),(\\d+)\\)")

        var isEnabled = true
        // Map the multiple lines to a flat list of match results
        val sumOfMultiples = data.flatMap {
            regex.findAll(it)
        // Filter out the mul's that are disabled and the dos and don'ts
        }.filter {
            val function = it.groupValues[0]
            if (function.startsWith("don't")) {
                isEnabled = false
                false
            }
            else if (function.startsWith("do")) {
                isEnabled = true
                false
            }
            else {
                isEnabled
            }
        // Calculate the sum of the multiplications
        }.sumOf {
            it.groupValues[1].toInt() * it.groupValues[2].toInt()
        }

        return sumOfMultiples.toString()
    }
}