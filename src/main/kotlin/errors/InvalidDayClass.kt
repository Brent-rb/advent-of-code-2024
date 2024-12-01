package be.brentberghmans.advent2024.errors

class InvalidDayClass(dayNumber: String): Exception("[InvalidDayClass] Day$dayNumber does not extend Day") {
}