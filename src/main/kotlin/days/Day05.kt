package be.brentberghmans.advent2024.days

import be.brentberghmans.advent2024.extensions.middle
import be.brentberghmans.advent2024.extensions.split
import be.brentberghmans.advent2024.extensions.toIntList
import be.brentberghmans.advent2024.models.Day

enum class PairOrder {
    After,
    Before
}
data class NumberPair(
    val a: Int,
    val b: Int
)

data class ConditionIndex(
    val number: Int,
    var index: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ConditionIndex) return false

        return number == other.number
    }

    override fun hashCode(): Int {
        return number.hashCode()
    }
}
class Day05: Day {
    // TODO: Make solution prettier
    // The idea how A works is pretty simple:
    // The input begins with order of X|Y, X has to come before Y
    // This means that if we encounter Y, X may not come after
    // So we keep a list of "banned" numbers and we iterate over the input
    // For each number we check if it is in the "banned" list
    // If so, we stop and don't count the line
    // If not, we retrieve all the pairs X|Y, where Y is current number and add all X's to the list of banned numbers
    // The idea is good, the implementation is ugly
    override fun solveA(data: List<String>): String {
        val dataTupple = data.split { it.isEmpty() }
        val pairList = dataTupple.a
        val inputList = dataTupple.b

        val pairs = pairList.map {
            val splits = it.split("|")
            NumberPair(splits[1].toInt(), splits[0].toInt())
        }

        val beforeMap = pairs.groupBy { it.a }
        var count = 0

        for (line in inputList) {
            /** If a number is in here, if encountered in the line it means it broke a condition */
            val activeBeforeSet = mutableSetOf<Int>()
            val entries = line.split(",").map { it.toInt() }
            var validLine = true
            for (number in entries) {
                if (activeBeforeSet.contains(number)) {
                    validLine = false
                    break
                }


                val beforeEntries = (beforeMap[number] ?: listOf()).map { it.b }
                activeBeforeSet.addAll(beforeEntries)
            }

            if (validLine) {
                count += line.toIntList(",").middle() ?: 0
            }
        }

        return count.toString()
    }

    // TODO improve solution
    // B is an adaptation of A
    // The core of the solution is the same, keep track of a list of numbers that are banned.
    // However instead of only keeping track of which numbers are banned, we also keep track of which index added this banned number
    // So we know that when we encountered an illegal number, we need to move it to that index
    //
    // We check every number, let call the number Y and the index Yi,
    // If it's not in the banned list, for all X|Y pairs we add the entry (X, Yi) to the list of banned numbers
    // If it is banned, we move Y to the index Yi where X = Y
    //
    // That is the core of the idea, however because we are changing the order of the items in the list
    // The indices of the banned list may no longer be correct so we need to do the following:
    // - For all entries (X, Xi) we need to increment Xi by 1 if Xi >= Yi,
    //      this is because all the items after Yi have now been shifted to the right
    // - We retrieve all pairs X|Y, if X already exists in the banned list AND Xi > Yi, we change the entry from (X, Xi) to (X, Yi)
    //      this is because the banned list keeps track of the indices of the EARLIEST entry that added this number
    //      because Y was moved to an earlier position it is possible that it's index is now earlier than the previous entry
    //      but it's also possible there are item even earlier in the list that added it and we don't want to overwrite those
    // - We add the remaining (X, Yi) entries to the list
    override fun solveB(data: List<String>): String {
        val dataTupple = data.split { it.isEmpty() }
        val pairList = dataTupple.a
        val inputList = dataTupple.b

        val pairs = pairList.map {
            val splits = it.split("|")
            NumberPair(splits[1].toInt(), splits[0].toInt())
        }

        val afterMap = pairs.groupBy { it.a }
        var count = 0
        var validCount = 0

        for (line in inputList) {
            /** If a number is in here, if encountered in the line it means it broke a condition */
            val illegalNumbers = mutableSetOf<ConditionIndex>()
            val entries = line.toIntList(",").toMutableList()
            var isCorrect = true

            for (i in entries.indices) {
                val number = entries[i]
                val condition = illegalNumbers.find { it.number == number }
                if (condition == null) {
                    val afterEntries = (afterMap[number] ?: listOf()).map { it.b }
                    illegalNumbers.addAll(afterEntries.map { ConditionIndex(it, i) })
                    continue
                }
                isCorrect = false

                val moveToIndex = condition.index
                illegalNumbers.forEach {
                    if (it.index >= moveToIndex) {
                        it.index += 1
                    }
                }

                entries.removeAt(i)
                entries.add(moveToIndex, number)

                val afterEntries = (afterMap[number] ?: listOf()).map { it.b }
                val addEntries = mutableListOf<Int>()
                afterEntries.forEach { afterNumber ->
                    val existingEntry = illegalNumbers.find { it.number == afterNumber }
                    if (existingEntry == null) {
                        addEntries.add(afterNumber)
                    }
                    if (existingEntry != null && existingEntry.index > moveToIndex) {
                        existingEntry.index = moveToIndex
                    }
                }
                illegalNumbers.addAll(addEntries.map { ConditionIndex(it, moveToIndex) })
            }

            if (!isCorrect) {
                println(entries)
                count += entries.middle() ?: 0
            }
            else {
                validCount += entries.middle() ?: 0
            }
        }

        return count.toString()
    }
}