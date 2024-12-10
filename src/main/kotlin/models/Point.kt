package be.brentberghmans.advent2024.models

data class Point(
    var x: Int,
    var y: Int
) {
    operator fun plus(point: Point): Point {
        return Point(x + point.x, y + point.y)
    }

    operator fun plus(direction: GridDirection): Point {
        return this + direction.increments
    }

    operator fun minus(point: Point): Point {
        return Point(x - point.x, y - point.y)
    }

    operator fun minus(direction: GridDirection): Point {
        return this - direction.increments
    }

    operator fun times(number: Int): Point {
        return Point(x * number, y * number)
    }

    operator fun unaryMinus(): Point {
        return Point(-x, -y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point) return false

        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    fun clone(): Point {
        return Point(x, y)
    }
}