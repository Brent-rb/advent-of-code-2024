package be.brentberghmans.advent2024.extensions

import be.brentberghmans.advent2024.models.Point
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
    if (isEmpty()) {
        return null
    }

    val index = floor(size.toFloat() / 2.0f).toInt()

    return this[index]
}

fun List<String>.toMutableGrid(): List<MutableList<Char>> {
    return this.map {
        it.toMutableList()
    }
}

fun <T> List<List<T>>.inBounds(pos: Point): Boolean {
    return pos.y in indices && pos.x in this[pos.y].indices
}

operator fun <T> List<List<T>>.get(pos: Point): T? {
    if (!inBounds(pos)) {
        return null
    }

    return this[pos.y][pos.x]
}

fun List<List<Char>>.findAll(char: Char): List<Point> {
    return findAll { it == char }
}

fun <T> List<List<T>>.findAll(predicate: (T) -> Boolean): List<Point> {
    val list = mutableListOf<Point>()

    forEachIndexed { y, row ->
        row.forEachIndexed { index, c ->
            if(predicate(c))
                list.add(Point(index, y))
        }
    }

    return list
}