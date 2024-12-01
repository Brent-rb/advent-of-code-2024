package be.brentberghmans.advent2024.errors

class DayCouldNotBeCreated(dayNumber: String): Exception("[DayCouldNotBeCreated] Day$dayNumber initiation resulted in null") {
}