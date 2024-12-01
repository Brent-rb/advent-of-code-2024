package be.brentberghmans.advent2024.utils

class ListUtils {
    companion object {
        fun <T> splitList(data: List<String>, splitter: String, transformValues: (item: String) -> T): List<List<T>> {
            return data.map { line ->
                line.split(splitter).map { transformValues(it) }
            }.filter { it.size == 2 }
        }

        fun splitList(data: List<String>, splitter: String): List<List<String>> {
            return data.map { line ->
                line.split(splitter)
            }.filter { it.size == 2 }
        }
    }
}