package be.brentberghmans.advent2024.extensions

import java.io.File

fun File.readNonEmptyLines(): List<String> {
    return readLines().filter { it.isNotEmpty() }
}