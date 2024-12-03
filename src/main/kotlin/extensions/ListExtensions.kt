package be.brentberghmans.advent2024.extensions

fun <T> List<T>.some(predicate: (T) -> Boolean): Boolean {
    return firstOrNull(predicate) != null
}