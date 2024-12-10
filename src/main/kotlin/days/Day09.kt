package be.brentberghmans.advent2024.days

import be.brentberghmans.advent2024.extensions.size
import be.brentberghmans.advent2024.models.Day
import java.io.File

data class FileData(
    var id: Int,
    var range: IntRange
)
class Day09: Day {
    override fun solveA(data: List<String>): String {
        val row = data[0]
        val fileData = mutableListOf<FileData>()
        val emptySpaces = mutableListOf<FileData>()
        var rangeStart = 0
        for (i in row.indices step 2) {
            val id = i / 2
            val dataSpaces = row[i].toString().toInt()
            val freeSpaces = if (i + 1 >= row.length) 0 else row[i + 1].toString().toInt()

            fileData.add(FileData(id, rangeStart until rangeStart + dataSpaces))
            emptySpaces.add(FileData(-1, rangeStart + dataSpaces until rangeStart + dataSpaces + freeSpaces))
            rangeStart += dataSpaces + freeSpaces
        }

        var firstEmptyIndex = emptySpaces[0].range.first
        var lastDataIndex = fileData.last().range.last

        // print(fileData, emptySpaces)
        while(lastDataIndex > firstEmptyIndex) {
            val firstEmpty = emptySpaces.first()
            val lastData = fileData.last()

            val emptyStart = firstEmpty.range.first
            val emptyEnd = firstEmpty.range.last
            val emptySize = firstEmpty.range.size()
            val dataStart = lastData.range.first
            val dataEnd = lastData.range.last
            val dataSize = lastData.range.size()

            if (emptySize > dataSize) {
                val insertIndex = fileData.indexOfFirst { it.range.first > emptyStart }
                fileData.add(insertIndex, FileData(lastData.id, emptyStart until emptyStart + dataSize))
                firstEmpty.range = (emptyStart + dataSize) .. emptyEnd
                fileData.removeLast()
            }
            else if (emptySize < dataSize) {
                val insertIndex = fileData.indexOfFirst { it.range.first > emptyStart }
                fileData.add(insertIndex, FileData(lastData.id, emptyStart until emptyStart + emptySize))
                lastData.range = dataStart .. dataEnd - emptySize
                emptySpaces.removeFirst()
            }
            else {
                val insertIndex = fileData.indexOfFirst { it.range.first > emptyStart }
                fileData.add(insertIndex, FileData(lastData.id, emptyStart until emptyStart + dataSize))
                fileData.removeLast()
                emptySpaces.removeFirst()
            }

            firstEmptyIndex = emptySpaces[0].range.first
            lastDataIndex = fileData.last().range.last

            // print(fileData, emptySpaces)
        }

        var index = 0
        var sum: Long = 0
        for (data in fileData) {
            for (x in data.range) {
                sum += data.id * index
                index++
            }
        }

        return sum.toString()
    }

    fun print(fileData: List<FileData>, emptyData: List<FileData>) {
        val joined = (fileData + emptyData).sortedBy { it.range.first }

        for (data in joined) {
            val char = if (data.id < 0) "." else data.id.toString()
            for (x in data.range) {
                print(char)
            }
        }
        println()
    }

    override fun solveB(data: List<String>): String {
        val row = data[0]
        val fileData = mutableListOf<FileData>()
        val emptySpaces = mutableListOf<FileData>()
        var rangeStart = 0
        for (i in row.indices step 2) {
            val id = i / 2
            val dataSpaces = row[i].toString().toInt()
            val freeSpaces = if (i + 1 >= row.length) 0 else row[i + 1].toString().toInt()

            fileData.add(FileData(id, rangeStart until rangeStart + dataSpaces))
            emptySpaces.add(FileData(-1, rangeStart + dataSpaces until rangeStart + dataSpaces + freeSpaces))
            rangeStart += dataSpaces + freeSpaces
        }

        // print(fileData, emptySpaces)
        val fileDataCopy = fileData.reversed()
        for(data in fileDataCopy) {
            val dataIndex = fileData.indexOfLast { it.id == data.id }
            val lastData = fileData[dataIndex]
            val emptySpaceIndex = emptySpaces.indexOfFirst { it.range.size() >= data.range.size() && it.range.first <  lastData.range.first }
            if (emptySpaceIndex < 0) {
                continue
            }

            val firstEmpty = emptySpaces[emptySpaceIndex]


            val emptyStart = firstEmpty.range.first
            val emptyEnd = firstEmpty.range.last
            val emptySize = firstEmpty.range.size()
            val dataSize = lastData.range.size()

            if (emptySize > dataSize) {
                val insertIndex = fileData.indexOfFirst { it.range.first > emptyStart }
                fileData.removeAt(dataIndex)
                fileData.add(insertIndex, FileData(lastData.id, emptyStart until emptyStart + dataSize))
                firstEmpty.range = (emptyStart + dataSize) .. emptyEnd
            }
            else {
                val insertIndex = fileData.indexOfFirst { it.range.first > emptyStart }
                fileData.removeAt(dataIndex)
                fileData.add(insertIndex, FileData(lastData.id, emptyStart until emptyStart + dataSize))
                emptySpaces.removeAt(emptySpaceIndex)
            }

            val preEmptyIndex = emptySpaces.indexOfFirst { it.range.last == lastData.range.first - 1 }
            val postEmptyIndex = emptySpaces.indexOfFirst { it.range.first == lastData.range.last + 1 }
            val preEmptyStart = if (preEmptyIndex != -1) { emptySpaces[preEmptyIndex].range.first } else lastData.range.first
            val postEmptyEnd = if (postEmptyIndex != -1) { emptySpaces[postEmptyIndex].range.last } else lastData.range.last
            val insertIndex = if (preEmptyIndex >= 0) preEmptyIndex else if (postEmptyIndex >= 0) postEmptyIndex else -1

            if (postEmptyIndex >= 0)
                emptySpaces.removeAt(postEmptyIndex)
            if (preEmptyIndex >= 0)
                emptySpaces.removeAt(preEmptyIndex)

            val actualInsertIndex = if (insertIndex >= 0) insertIndex else emptySpaces.indexOfFirst { it.range.start > preEmptyStart }
            emptySpaces.add(actualInsertIndex, FileData(-1, preEmptyStart .. postEmptyEnd))

            // print(fileData, emptySpaces)
        }

        var index = 0
        var sum: Long = 0

        val joined = (fileData + emptySpaces).sortedBy { it.range.first }
        for (data in joined) {
            if (data.id > 0) {
                for (x in data.range) {
                    sum += data.id * index
                    index++
                }
            }
            else {
                index += data.range.size()
                continue
            }
        }
        println()

        return sum.toString()
    }
}