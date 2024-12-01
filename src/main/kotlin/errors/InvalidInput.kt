package be.brentberghmans.advent2024.errors

class InvalidInput(file: String): Exception("[InvalidInput] $file") {
}