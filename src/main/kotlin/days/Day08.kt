package be.brentberghmans.advent2024.days

import be.brentberghmans.advent2024.extensions.findAll
import be.brentberghmans.advent2024.extensions.inBounds
import be.brentberghmans.advent2024.extensions.toMutableGrid
import be.brentberghmans.advent2024.models.Day
import be.brentberghmans.advent2024.models.Point

class Day08: Day {
    override fun solveA(data: List<String>): String {
        val grid = data.toMutableGrid()

        val chars = grid.flatMap { row ->
            row.filter { it != '.' }
        }.toSet()

        val charPoints = chars.map {
            Pair(it, grid.findAll(it))
        }.groupBy {
            it.first
        }

        val nodes = mutableSetOf<Point>()
        chars.forEach {char ->
            val points = charPoints[char]?.flatMap { it.second } ?: listOf()

            points.indices.forEach { index ->
                val mainPoint = points[index]

                for (i in index + 1 until points.size) {
                    val otherPoint = points[i]

                    val delta = otherPoint.minus(mainPoint)
                    val node1 = mainPoint - delta
                    val node2 = otherPoint + delta

                    if (grid.inBounds(node1)) {
                        nodes.add(node1)
                    }

                    if (grid.inBounds(node2)) {
                        nodes.add(node2)
                    }
                }
            }
        }

        return nodes.size.toString()
    }

    override fun solveB(data: List<String>): String {
        val grid = data.toMutableGrid()

        val chars = grid.flatMap { row ->
            row.filter { it != '.' }
        }.toSet()

        val charPoints = chars.map {
            Pair(it, grid.findAll(it))
        }.groupBy {
            it.first
        }

        val nodes = mutableSetOf<Point>()
        chars.forEach { char ->
            val points = charPoints[char]?.flatMap { it.second } ?: listOf()

            points.indices.forEach { index ->
                val mainPoint = points[index]

                for (i in index + 1 until points.size) {
                    val otherPoint = points[i]

                    val delta = otherPoint.minus(mainPoint)
                    var multiplier = -1

                    while (true) {
                        var hasNode1 = false
                        var hasNode2 = false

                        val node1 = mainPoint - (delta * multiplier)
                        val node2 = otherPoint + (delta * multiplier)

                        if (grid.inBounds(node1)) {
                            hasNode1 = true
                            nodes.add(node1)
                        }

                        if (grid.inBounds(node2)) {
                            hasNode2 = true
                            nodes.add(node2)
                        }

                        multiplier += 1
                        if (!hasNode1 && !hasNode2) {
                            break
                        }
                    }
                }
            }
        }

        return nodes.size.toString()
    }
}