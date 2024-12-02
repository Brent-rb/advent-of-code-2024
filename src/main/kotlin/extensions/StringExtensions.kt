package be.brentberghmans.advent2024.extensions


fun String.toIntList(seperator: String = " "): List<Int> {
    return split(seperator).map { it.toInt() }
}