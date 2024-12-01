package be.brentberghmans.advent2024.models

import be.brentberghmans.advent2024.errors.InvalidIoFilename

class DayInfo {
    val number: String
    val part: String
    val isExample: Boolean
    val isA: Boolean
    val isB: Boolean
    constructor(filename: String) {
        val splits = filename.lowercase().split("-")
        if (splits.size != 3) {
            throw InvalidIoFilename(filename)
        }

        number = splits[1]
        part = splits[2].removeSuffix(".txt")
        isExample = part.endsWith(".example")
        isA = part.startsWith("a")
        isB = part.startsWith("b")
    }
    fun getKey(): String {
        return "day-${number}-${part}"
    }

    fun getLogKey(): String {
        val partName = if (isA) "a" else "b"
        val suffix = if (isExample) "e" else "*"

        return "day-${number}-$partName-$suffix"
    }
}
