package be.brentberghmans.advent2024.extensions


fun IntRange.size(): Int {
    return (endInclusive + 1) - start
}