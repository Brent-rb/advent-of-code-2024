package be.brentberghmans.advent2024.extensions

import be.brentberghmans.advent2024.models.Tupple
import kotlin.math.floor

fun <T> List<T>.some(predicate: (T) -> Boolean): Boolean {
    return firstOrNull(predicate) != null
}

fun <T> List<T>.findLastIndex(predicate: (T) -> Boolean): Int {
    for (i in size - 1 downTo 0) {
        if (predicate(this[i])) {
            return i
        }
    }

    return -1
}

fun List<String>.filterEmpty(): List<String> {
    return this.filter { it.isNotEmpty() }
}

fun <T> List<T>.split(predicate: (T) -> Boolean): Tupple<List<T>, List<T>> {
    val splitIndex = indexOfFirst(predicate)
    if (splitIndex == -1) {
        return Tupple(this, listOf())
    }

    return Tupple(subList(0, splitIndex), subList(splitIndex + 1, size))
}

fun <T> List<T>.middle(): T? {
    if (size == 0) {
        return null
    }

    val index = floor(size.toFloat() / 2.0f).toInt()

    return this[index]
}