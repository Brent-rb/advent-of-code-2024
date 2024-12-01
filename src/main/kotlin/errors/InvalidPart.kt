package be.brentberghmans.advent2024.errors

class InvalidPart(part: String): Exception("[InvalidPart] part $part is unsupported") {
}