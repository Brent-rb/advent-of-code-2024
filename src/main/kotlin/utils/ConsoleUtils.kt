package be.brentberghmans.advent2024.utils

class ConsoleUtils {
    companion object {
        fun readYesNo(question: String): Boolean {
            do {
                print(question)
                val input = readlnOrNull()

                if (input?.trim() == "y") {
                    return true
                }
                else if (input?.trim() == "n") {
                    return false
                }
            } while(true)
        }
    }
}