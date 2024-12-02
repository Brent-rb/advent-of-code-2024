package be.brentberghmans.advent2024.utils

class ListUtils {
    companion object {
        fun <T> getVerticalLists(data: List<String>, splitter: String, length: Int = 2, transformValues: (item: String) -> T): List<List<T>> {
            return data.map { line ->
                line.split(splitter).map { transformValues(it) }
            }.filter { it.size == length }
        }

        fun getVerticalLists(data: List<String>, splitter: String, length: Int = 2): List<List<String>> {
            return data.map { line ->
                line.split(splitter)
            }.filter { it.size == length }
        }
    }
}