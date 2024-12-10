package be.brentberghmans.advent2024.days

import be.brentberghmans.advent2024.extensions.toIntList
import be.brentberghmans.advent2024.extensions.toLongList
import be.brentberghmans.advent2024.models.Day

class Day07: Day {
    fun determine(target: Long, numbers: List<Long>): Boolean {
        val plus = numbers[0] + numbers[1]
        val mul = numbers[0] * numbers[1]
        if (numbers.size == 2) {
            return (plus == target || mul == target)
        }

        val subList = numbers.subList(2, numbers.size)
        return determine(target, listOf(plus) + subList) || determine(target, listOf(mul) + subList)
    }

    fun determineB(target: Long, numbers: List<Long>): Boolean {
        val one = numbers[0]
        val two = numbers[1]
        val plus = one + two
        val mul = one * two
        val concat = "$one$two".toLong()
        if (numbers.size == 2) {
            return (plus == target || mul == target || concat == target)
        }

        val subList = numbers.subList(2, numbers.size)
        return determineB(target, listOf(plus) + subList) || determineB(target, listOf(mul) + subList) || determineB(target, listOf(concat) + subList)
    }
    override fun solveA(data: List<String>): String {
        return data.sumOf { row ->
            val colonSplit = row.split(":")
            val answer = colonSplit[0].trim()
            val numbers = colonSplit[1].trim().toLongList(" ")

            if (determine(answer.toLong(), numbers)) {
                answer.toLong()
            }
            else {
                0L
            }
        }.toString()
    }

    override fun solveB(data: List<String>): String {
        return data.sumOf { row ->
            val colonSplit = row.split(":")
            val answer = colonSplit[0].trim()
            val numbers = colonSplit[1].trim().toLongList(" ")

            if (determineB(answer.toLong(), numbers)) {
                answer.toLong()
            }
            else {
                0L
            }
        }.toString()
    }
}