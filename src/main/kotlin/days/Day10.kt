package be.brentberghmans.advent2024.days

import be.brentberghmans.advent2024.extensions.findAll
import be.brentberghmans.advent2024.extensions.get
import be.brentberghmans.advent2024.models.Day
import be.brentberghmans.advent2024.models.GridDirection
import be.brentberghmans.advent2024.models.Point

class Day10: Day {
    private fun findPath(data: List<List<Int>>, target: Int, previous: Int, previousPos: Point, discoveredEnds: MutableSet<Point>, discoveredPaths: MutableSet<List<Point>>, path: List<Point> = listOf(previousPos)) {
        if (previous == target) {
            discoveredEnds.add(previousPos)
            discoveredPaths.add(path)
            return
        }

        val upPos = previousPos + GridDirection.Up.increments
        val rightPos = previousPos + GridDirection.Right.increments
        val downPos = previousPos + GridDirection.Down.increments
        val leftPos = previousPos + GridDirection.Left.increments
        val up = data[upPos]
        val right = data[rightPos]
        val down = data[downPos]
        val left = data[leftPos]

        if (up == previous + 1) {
            findPath(data, target, up, upPos, discoveredEnds, discoveredPaths, path + upPos)
        }
        if (right == previous + 1) {
            findPath(data, target, right, rightPos, discoveredEnds, discoveredPaths, path + rightPos)
        }
        if (down == previous + 1) {
            findPath(data, target, down, downPos, discoveredEnds, discoveredPaths, path + downPos)
        }
        if (left == previous + 1) {
            findPath(data, target, left, leftPos, discoveredEnds, discoveredPaths, path + leftPos)
        }
    }

    override fun solveA(data: List<String>): String {
        val grid = data.map { row -> row.map { it.toString().toInt() }}
        val startingPositions = grid.findAll { it == 0 }

        return startingPositions.sumOf {
            val endList = mutableSetOf<Point>()
            findPath(grid, 9, 0, it, endList, mutableSetOf())
            endList.size
        }.toString()
    }

    override fun solveB(data: List<String>): String {
        val grid = data.map { row -> row.map { it.toString().toInt() }}
        val startingPositions = grid.findAll { it == 0 }

        return startingPositions.sumOf {
            val pathList = mutableSetOf<List<Point>>()
            findPath(grid, 9, 0, it, mutableSetOf(), pathList)
            pathList.size
        }.toString()
    }
}