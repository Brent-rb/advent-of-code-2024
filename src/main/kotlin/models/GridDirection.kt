package be.brentberghmans.advent2024.models

enum class GridDirection {
    Up,
    Right,
    Down,
    Left;

    val increments: Point
        get() = when(this) {
            Up -> Point(0, -1)
            Right -> Point(1, 0)
            Down -> Point(0, 1)
            Left -> Point(-1, 0)
        }

    val right: GridDirection
        get() = when(this) {
            Up -> Right
            Right -> Down
            Down -> Left
            Left -> Up
        }
}