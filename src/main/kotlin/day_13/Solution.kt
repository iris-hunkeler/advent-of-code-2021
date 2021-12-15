package day_13

import InputType
import java.io.File
import java.util.Collections.max

fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)

//    val part1 = countDotsAfterFirstFold(input)
//    println("*** Result 1: $part1 ***")

    printInstructionsAfterAllFolds(input)
    println("*** Result 2: check console ***")
}

fun countDotsAfterFirstFold(input: TransparentPaper): Int {
    println("Original paper")
    println(input)

    input.executeNextFold()
    println("After first fold")
    println(input)

    return input.countDots()
}

fun printInstructionsAfterAllFolds(input: TransparentPaper) {
    println("Original paper")

    input.executeAllRemainingFolds()

    println("\n After all folds")
    println(input)
}

private fun getInput(inputType: InputType): TransparentPaper {
    val dotCoordinates = File("src/main/kotlin/day_13/${inputType.fileName}.txt")
        .readLines()
        .filter { it.contains(",") }
        .map {
            val (x, y) = it.split(",")
            Pair(x.toInt(), y.toInt())
        }.toMutableList()

    val foldInstructions = File("src/main/kotlin/day_13/${inputType.fileName}.txt")
        .readLines()
        .filter { it.contains("fold along") }
        .map {
            val (side, number) = it.split(" ")[2].split("=")
            FoldInstruction(side, number.toInt())
        }.toList()

    val grid = TransparentPaper(foldInstructions = foldInstructions)
    grid.init(max(dotCoordinates.map { it.first }) + 1, max(dotCoordinates.map { it.second }) + 1)

    dotCoordinates.forEach {
        grid.dotGrid[it.second][it.first] = true
    }

    return grid
}

