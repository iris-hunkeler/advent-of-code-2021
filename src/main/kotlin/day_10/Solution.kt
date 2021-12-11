package day_10

import InputType
import java.io.File

fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)

    val part1 = calculateSyntaxErrorScoreForCorruptLines(input)
    println("*** Result 1: $part1 ***")

    val part2 = calculateAutocompleteScoreForIncompleteLines(input)
    println("*** Result 2: $part2 ***")
}

fun calculateSyntaxErrorScoreForCorruptLines(input: List<String>): Int {
    var syntaxScore = 0

    input.forEach { line ->
        syntaxScore += getSyntaxScoreForLine(line)
    }

    return syntaxScore
}

private fun getSyntaxScoreForLine(line: String): Int {
    val stack = ArrayDeque<Char>()

    line.forEach { char ->
        if (ChunkConfig.isOpening(char)) {
            stack.add(char)
        } else if (ChunkConfig.isClosing(char)) {
            val config = ChunkConfig.findByClosing(char)
            val last = stack.removeLast()
            if (last != config.opening) {
                return config.syntaxScore
            }
        }
    }
    return 0
}

fun calculateAutocompleteScoreForIncompleteLines(input: List<String>): Long {
    val autoCompleteScores = mutableListOf<Long>()

    input.forEach { line ->
        getAutocompleteScoreForLine(line)?.let {
            autoCompleteScores.add(it)
        }
    }

    return autoCompleteScores.sorted()[autoCompleteScores.size/2]
}

private fun getAutocompleteScoreForLine(line: String): Long? {
    val stack = ArrayDeque<Char>()

    line.forEach { char ->
        if (ChunkConfig.isOpening(char)) {
            stack.add(char)
        } else if (ChunkConfig.isClosing(char)) {
            val config = ChunkConfig.findByClosing(char)
            val last = stack.removeLast()
            if (last != config.opening) {
                return null
            }
        }
    }

    var score = 0L
    while(stack.isNotEmpty()) {
        val last = stack.removeLast()
        score *= 5
        score += ChunkConfig.findAutoCompleteScoreByOpening(last)
    }

    return score
}

enum class ChunkConfig(
    val opening: Char,
    val closing: Char,
    val syntaxScore: Int,
    val autoCompleteScore: Int
) {
    ROUND('(', ')', 3, 1),
    SQUARE('[', ']', 57, 2),
    CURLY('{', '}', 1197, 3),
    POINTY('<', '>', 25137, 4);

    companion object {
        fun isOpening(char: Char) = values().map { it.opening }.contains(char)
        fun isClosing(char: Char) = values().map { it.closing }.contains(char)

        fun findByClosing(char: Char) = values().find { it.closing == char } ?: error("No config found for closing char $char")
        fun findAutoCompleteScoreByOpening(char: Char) = values().find { it.opening == char }?.autoCompleteScore ?: error("No config found for opening char $char")
    }
}

private fun getInput(inputType: InputType): List<String> {
    return File("src/main/kotlin/day_10/${inputType.fileName}.txt")
        .readLines()
}
