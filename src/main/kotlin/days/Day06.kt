package be.brentberghmans.advent2024.days

import be.brentberghmans.advent2024.extensions.get
import be.brentberghmans.advent2024.extensions.inBounds
import be.brentberghmans.advent2024.extensions.toMutableGrid
import be.brentberghmans.advent2024.models.Day
import be.brentberghmans.advent2024.models.GridDirection
import be.brentberghmans.advent2024.models.GridVector
import be.brentberghmans.advent2024.models.Point
import kotlin.math.abs


/**
 * DAY 6 IS NOT CORRECT AT THE MOMENT
 * MY INITIAL SOLUTION MADE USE OF THE WALL-SKIP TECHNIQUE LIKE IT IS USING BELOW
 * HOWEVER, I COULD NOT GET IT TO WORK
 *
 * I IMPLEMENTED A BRUTE-FORCE SOLUTION THAT WALKS ALL PATHS EVEN THE CYCLES
 * THAT GAVE ME THE CORRECT ANSWER
 * HOWEVER I DID NOT COMMIT IT
 *
 * AFTER MY WORKING SOLUTION GOT THE ANSWER I CAME BACK TO THE PROBLEM TO CHECK IF THE
 * OPTIMIZATION COULD BE MADE TO WORK
 *
 * AFTER A LOT OF LOST SLEEP I COULDN'T FIGURE OUT THE ISSUE AND THUS THIS IS THE STATE IT IS LEFT IN
 */

fun getWalls(grid: List<List<Char>>): List<Point> {
    return grid.flatMapIndexed { y: Int, chars: List<Char> ->
        val list = mutableListOf<Point>()
        chars.forEachIndexed { x, c ->
            if (c == '#') {
                list.add(Point(x, y))
            }
        }

        list
    }
}

fun charToDirection(char: Char): GridDirection {
    return when (char) {
        '<' -> GridDirection.Left
        '>' -> GridDirection.Right
        '^' -> GridDirection.Up
        'v' -> GridDirection.Down
        else -> throw Exception("Invalid direction char $char")
    }
}

fun findGuard(grid: List<List<Char>>): GridVector {
    val directions = listOf('^', 'v', '>', '<')

    grid.forEachIndexed { y, chars ->
        chars.forEachIndexed { x, c ->
            if (directions.contains(c)) {
                return GridVector(Point(x, y), charToDirection(c))
            }
        }
    }

    throw Exception("No guard found")
}

fun getClosestWall(wallPos: List<Point>, pos: Point, direction: GridDirection): Point? {
    return wallPos.filter { wall ->
        when (direction) {
            GridDirection.Up -> wall.x == pos.x && wall.y < pos.y
            GridDirection.Right -> wall.y == pos.y && wall.x > pos.x
            GridDirection.Down -> wall.x == pos.x && wall.y > pos.y
            GridDirection.Left -> wall.y == pos.y && wall.x < pos.x
        }
    }.minByOrNull {
        when (direction) {
            GridDirection.Up -> abs(it.y - pos.y)
            GridDirection.Right -> abs(it.x - pos.x)
            GridDirection.Down -> abs(it.y - pos.y)
            GridDirection.Left -> abs(it.x - pos.x)
        }
    }
}

fun getSteps(grid: List<List<Char>>, obstruction: Point? = null): List<Point>? {
    val start = findGuard(grid)
    val startPos = start.pos

    var pos = startPos.clone()
    var dir = start.direction
    val visitedPos = mutableSetOf<Point>()
    val turningPoints = mutableSetOf<Pair<Point, GridDirection>>()

    while (grid.inBounds(pos)) {
        val curVector = pos to dir
        val nextPos = pos + dir

        visitedPos.add(pos)

        if (grid.inBounds(nextPos) && (grid[nextPos] == '#' || obstruction == nextPos)) {
            if (turningPoints.contains(curVector)) {
                return null
            }
            turningPoints.add(curVector)
            dir = dir.right
        }
        else {
            pos = nextPos
        }
    }

    return visitedPos.toList()
}

fun print(grid: List<List<Char>>, obstruction: Point) {
    grid.forEachIndexed { y, chars ->
        if (y != obstruction.y) {
            println(chars.joinToString(""))
        }
        else {
            chars.forEachIndexed { index, c ->
                if (index != obstruction.x) {
                    print(c)
                }
                else {
                    print("O")
                }
            }
            println()
        }
    }
    println()
}

fun isCycle(wallPos: List<Point>, pos: Point, direction: GridDirection, turnedPoints: Set<Pair<Point, GridDirection>> = setOf()): Boolean {
    val turnPoint = getClosestWall(wallPos, pos, direction)?.minus(direction) ?: return false
    val nextPoint = getClosestWall(wallPos, turnPoint, direction.right)?.minus(direction.right) ?: return false
    if (turnedPoints.contains(turnPoint to direction) && turnedPoints.contains(nextPoint to direction.right)) {
        return true
    }

    return isCycle(wallPos, turnPoint, direction.right, turnedPoints + (turnPoint to direction))
}

fun getCycles(grid: List<List<Char>>): Int {
    val walls = getWalls(grid)
    val start = findGuard(grid)

    var pos = start.pos.clone()
    var dir = start.direction
    var cycles = 0
    val cyclePoints = mutableSetOf<Point>()

    while (grid.inBounds(pos)) {
        val nextPos = pos + dir
        val nextInBounds = grid.inBounds(nextPos)

        if (nextInBounds &&
            grid[nextPos] != '#' &&
            grid[nextPos] != '^' &&
            !cyclePoints.contains(nextPos) &&
            isCycle(walls + nextPos, pos, dir.right)) {

            cycles++
            cyclePoints.add(nextPos)
        }

        if (nextInBounds && grid[nextPos] == '#' ) {
            dir = dir.right
        }
        else {
            pos = nextPos
        }
    }

    return cycles
}

class Day06: Day {
    override fun solveA(data: List<String>): String {
        val grid = data.toMutableGrid()
        return getSteps(grid)?.size.toString()
    }

    // 1655
    override fun solveB(data: List<String>): String {
        val grid = data.toMutableGrid()

        return getCycles(grid).toString()
    }
}