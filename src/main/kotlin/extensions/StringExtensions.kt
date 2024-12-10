package be.brentberghmans.advent2024.extensions


fun String.toIntList(seperator: String = " "): List<Int> {
    return split(seperator).map { it.toInt() }
}
fun String.toLongList(seperator: String = " "): List<Long> {
    return split(seperator).map { it.toLong() }
}