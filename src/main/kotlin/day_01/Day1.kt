package day_01

import java.io.File


fun main(args: Array<String>) {
    val input = getInput()

    val part1 = calculateIncreases(input)
    println("Part 1: $part1")

    val part2 = calculateIncreasesOfSlidingWindow(input)
    println("Part 2: $part2")
}

fun calculateIncreases(input: List<Int>): Int {
    var count = -1
    input.forEachIndexed { index, current ->
        val previous = if(index == 0) {
            0
        } else {
            input[index-1]
        }

        if(current > previous) {
            count++
        }
    }

    return count
}

fun calculateIncreasesOfSlidingWindow(input: List<Int>): Int {
    val slidingWindows = mutableListOf<Int>()
    for(i in 2 until input.size) {
        slidingWindows.add(input[i-2] + input [i-1] + input[i])
    }

    val count = calculateIncreases(slidingWindows)
    return count
}

private fun getSampleInput(): List<Int> {
    return File("src/main/kotlin/day_01/sample.txt")
        .readLines()
        .map(String::toInt)
}


private fun getInput(): List<Int> {
    return File("src/main/kotlin/day_01/input.txt")
        .readLines()
        .map(String::toInt)
}