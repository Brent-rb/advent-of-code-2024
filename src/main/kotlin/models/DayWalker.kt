package be.brentberghmans.advent2024.models

import be.brentberghmans.advent2024.errors.*
import be.brentberghmans.advent2024.extensions.readNonEmptyLines
import be.brentberghmans.advent2024.utils.ConsoleUtils
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.reflect.full.createInstance

/**
 * Iterates ("Walks") over the inputs in the 'io' folder of the project and executes the correct Day class depending on the name.
 *
 * This class iterates over the files that are present in the 'io' folder in the root of the project.
 * The files are expected to be named in this format: day-xx-y.txt
 * Where xx is the day, with a 0 prefix if needed, and y is any of a, a.example, b, b.example
 *
 * The class will then search for a class with the name DayXX in the project using reflection. (This class should extend the Day class)
 * If the class is found, it creates an instance and executes the solveA and solveB functions with the input from the files.
 *
 * It also keeps track of which days have already been solved so that compute time is not wasted (later problems can sometimes take a long time)
 * Examples are always executed though, so keep them simple!
 *
 * For example files, the last line of the file is expected to contain the answer
 */
class DayWalker {
    private val _solvedMap = mutableMapOf<String, Boolean>()
    private val _solvedExamples = mutableMapOf<String, Boolean>()

    fun solve() {
        load()
        solveAll()
        save()
    }

    private fun load() {
        val cachePath = Path("io/cache.txt")
        if (!cachePath.exists()) {
            return
        }

        val items = cachePath.toFile().readLines()
        for (item in items) {
            _solvedMap[item.trim()] = true
        }
    }

    private fun save() {
        val cachePath = Path("io/cache.txt")
        if (!cachePath.exists()) {
            cachePath.toFile().createNewFile()
        }

        val file = cachePath.toFile()
        val writer = file.writer()

        _solvedMap.forEach { (key, value) ->
            if (value) writer.appendLine(key)
        }

        writer.close()
    }

    private fun solveAll() {
        println()

        val daysIo = getSortedDayInputList()
        daysIo.forEach {
            val dayInfo = DayInfo(it.fileName.toString())
            try {

                if (isSolved(dayInfo)) {
                    println("[${dayInfo.getLogKey()}] skipping, already solved")
                    return@forEach;
                }

                val dayInstance = createDayInstance(dayInfo.number)
                val isSolved = solveDay(it, dayInfo, dayInstance)
                setSolved(dayInfo, isSolved)
            }
            catch (error: Exception) {
                println("[${dayInfo.getLogKey()}] $error")
            }
        }
    }

    /**
     * Returns the list of input files from the 'io' folder, sorted by day number, day part, and example status.
     */
    private fun getSortedDayInputList(): List<Path> {
        return Path("io").listDirectoryEntries("day-*.txt").sortedWith { left, right ->
            val leftString = left.fileName.toString()
            val rightString = right.fileName.toString()

            val leftDayInfo = DayInfo(leftString)
            val rightDayInfo = DayInfo(rightString)

            // Sort by numbers
            val numberCompare = leftDayInfo.number.compareTo(rightDayInfo.number)
            // Only if numbers are different
            if (numberCompare != 0) {
                return@sortedWith numberCompare
            }

            // A before B
            if (leftDayInfo.isA && rightDayInfo.isB) {
                return@sortedWith -1
            }
            if (leftDayInfo.isB && rightDayInfo.isA) {
                return@sortedWith 1
            }

            // Is both are A or B, examples before non-examples
            if (leftDayInfo.isExample && !rightDayInfo.isExample) {
                return@sortedWith -1
            }
            if (!leftDayInfo.isExample && rightDayInfo.isExample) {
                return@sortedWith 1
            }

            // If both are A or B, and both are examples, they are equal
            return@sortedWith  0
        }
    }

    private fun isSolved(dayInfo: DayInfo): Boolean {
        return _solvedMap[dayInfo.getKey()] ?: false
    }

    private fun setSolved(info: DayInfo, solved: Boolean) {
        if (!info.isExample)
            _solvedMap[info.getKey()] = solved;
        else
            _solvedExamples[info.getKey()] = solved
    }

    private fun solveDay(path: Path, dayInfo: DayInfo, day: Day): Boolean {
        if (dayInfo.isExample) {
            return solveExample(path, dayInfo, day)
        }

        val exampleSolved = _solvedExamples["${dayInfo.getKey()}.example"] ?: false
        if (!exampleSolved) {
            println("[${dayInfo.getLogKey()}] has not been solved, returning false")
            return false
        }
        return solvePart(path, dayInfo, day)
    }

    private fun solveExample(path: Path, dayInfo: DayInfo, day: Day): Boolean {
        val exampleInfo = readExample(path)

        val solution = when (dayInfo.part) {
            "a.example" -> day.solveA(exampleInfo.data)
            "b.example" -> day.solveB(exampleInfo.data)
            else -> throw InvalidPart(dayInfo.part)
        }

        val isExampleCorrect = solution.trim() == exampleInfo.solution.trim()
        println("[${dayInfo.getLogKey()}] example is ${if (isExampleCorrect) "correct" else "wrong"}")
        return isExampleCorrect
    }

    private fun solvePart(path: Path, dayInfo: DayInfo, day: Day): Boolean {
        val data = path.toFile().readNonEmptyLines()

        val solution = when (dayInfo.part) {
            "a" -> day.solveA(data)
            "b" -> day.solveB(data)
            else -> throw InvalidPart(dayInfo.part)
        }

        println("[${dayInfo.getLogKey()}] solution: $solution")
        return ConsoleUtils.readYesNo("\tWas the solution correct? (y/n): ")
    }

    private fun readExample(path: Path): ExampleInfo {
        val lines = path.toFile().readNonEmptyLines()
        if (lines.isEmpty()) {
            throw InvalidInput(path.fileName.toString())
        }

        return ExampleInfo(
            lines.subList(0, lines.size - 1),
            lines[lines.size - 1]
        )
    }

    private fun createDayInstance(dayNumber: String): Day {
        val dayClass = Class.forName("be.brentberghmans.advent2024.days.Day$dayNumber").kotlin
        if (!Day::class.java.isAssignableFrom(dayClass.java)) {
            throw InvalidDayClass(dayNumber)
        }

        val day = dayClass.createInstance() as Day?
        return day ?: throw DayCouldNotBeCreated(dayNumber)
    }
}