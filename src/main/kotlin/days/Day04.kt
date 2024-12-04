package be.brentberghmans.advent2024.days

import be.brentberghmans.advent2024.models.Day


fun List<String>.inBounds(x: Int, y: Int): Boolean {
    return y in indices && x in 0 until this[y].length
}

enum class Direction {
    North,
    NorthEast,
    East,
    SouthEast,
    South,
    SouthWest,
    West,
    NorthWest
}

class Day04: Day {
    private fun hasStringInDirection(data: List<String>, x: Int, y: Int, direction: Direction, searchString: String): Boolean {
        val xIncrement = when(direction) {
            Direction.North -> 0
            Direction.NorthEast -> 1
            Direction.East -> 1
            Direction.SouthEast -> 1
            Direction.South -> 0
            Direction.SouthWest -> -1
            Direction.West -> -1
            Direction.NorthWest -> -1
        }
        val yIncrement = when(direction) {
            Direction.North -> -1
            Direction.NorthEast -> -1
            Direction.East -> 0
            Direction.SouthEast -> 1
            Direction.South -> 1
            Direction.SouthWest -> 1
            Direction.West -> 0
            Direction.NorthWest -> -1
        }

        var matchString = "${data[y][x]}"
        var searchX = x
        var searchY = y

        do {
            searchX += xIncrement
            searchY += yIncrement

            if (!data.inBounds(searchX, searchY)) {
                return false
            }

            matchString += data[searchY][searchX]
            if (matchString == searchString) {
                return true
            }
            else if (!searchString.startsWith(matchString)) {
                return false
            }
        } while (true)
    }
    private fun searchXmas(data: List<String>, x: Int, y: Int): Int {
        return Direction.entries.count {
            hasStringInDirection(data, x, y, it, "XMAS")
        }
    }
    override fun solveA(data: List<String>): String {
        var xmasCount = 0
        for (y in data.indices) {
            val row = data[y]
            for (x in row.indices) {
                val char = row[x]
                val isX = char == 'X'
                if (isX) {
                    xmasCount += searchXmas(data, x, y)
                }
            }
        }

        return xmasCount.toString()
    }

    override fun solveB(data: List<String>): String {
        var xmasCount = 0
        for (y in data.indices) {
            val row = data[y]
            for (x in row.indices) {
                val char = row[x]
                if (char != 'S' && char != 'M') {
                    continue
                }

                if (x + 2 >= row.length) {
                    continue
                }

                if (hasStringInDirection(data, x, y, Direction.SouthEast, "MAS") || hasStringInDirection(data, x, y, Direction.SouthEast, "SAM")) {
                    // We have string
                    // M . .     S . .
                    // . A .  or . A .
                    // . . S     . . M
                    // So we only need to check x + 2 south-west for SAM or MAS
                    // Because we only search SouthEast in the previous if, and we only check X+2 we prevent duplicate answers.

                    if (hasStringInDirection(data, x + 2, y, Direction.SouthWest, "MAS") || hasStringInDirection(data, x + 2, y, Direction.SouthWest, "SAM")) {
                        xmasCount++
                    }
                }
            }
        }

        return xmasCount.toString()
    }
}