package day_02

import InputType
import java.io.File

fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)

    val part1 = calculatePosition(input)
    println("*** Part 1: $part1 ***")


    val part2 = calculatePositionWithAim(input)
    println("*** Part 2: $part2 ***")
}

fun calculatePosition(input: List<Pair<String, Int>>): Int {
    var horizontal = 0
    var depth = 0

    input.forEach {
        when (it.first) {
            "forward" -> horizontal = horizontal + it.second
            "up" -> depth = depth - it.second
            "down" -> depth = depth + it.second
            else -> error("unknown input $it.first")
        }
    }

    println("horizontal: $horizontal")
    println("depth: $depth")

    return horizontal * depth
}

fun calculatePositionWithAim(input: List<Pair<String, Int>>): Int {
    var horizontal = 0
    var depth = 0
    var aim = 0

    input.forEach {
        when (it.first) {
            "forward" -> {
                horizontal = horizontal + it.second
                depth = depth + (aim * it.second)
            }
            "up" -> aim = aim - it.second
            "down" -> aim = aim + it.second
            else -> error("unknown input $it.first")
        }
    }

    println("horizontal: $horizontal")
    println("depth: $depth")
    println("aim: $aim")

    return horizontal * depth
}


private fun getInput(inputType: InputType): List<Pair<String, Int>> {
    return File("src/main/kotlin/day_02/${inputType.fileName}.txt")
        .readLines()
        .map {
            val inputs = it.split(" ")
            Pair(inputs[0], inputs[1].toInt())
        }
}
