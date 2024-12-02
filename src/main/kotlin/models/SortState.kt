package be.brentberghmans.advent2024.models

enum class SortState {
    Unknown,
    Equal,
    Ascending,
    Descending;

    companion object {
        fun from(a: Int, b: Int): SortState {
            return when {
                a < b -> Ascending
                a > b -> Descending
                else -> Equal
            }
        }
    }
}