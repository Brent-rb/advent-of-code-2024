package be.brentberghmans.advent2024.days

import be.brentberghmans.advent2024.models.Day

class Day03: Day {
    override fun solveA(data: List<String>): String {
        val regex = Regex("mul\\((\\d+),(\\d+)\\)")

        val sumOfMultiples = data.flatMap {
            regex.findAll(it)
        }.sumOf {
            it.groupValues[1].toInt() * it.groupValues[2].toInt()
        }

        return sumOfMultiples.toString()
    }

    override fun solveB(data: List<String>): String {
        val regex = Regex("do\\(\\)|don't\\(\\)|mul\\((\\d+),(\\d+)\\)")

        var isEnabled = true
        val sumOfMultiples = data.flatMap {
            regex.findAll(it)
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
        }.sumOf {
            it.groupValues[1].toInt() * it.groupValues[2].toInt()
        }

        return sumOfMultiples.toString()
    }
}