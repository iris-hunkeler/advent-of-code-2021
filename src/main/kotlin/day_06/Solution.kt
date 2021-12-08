package day_06

import InputType
import java.io.File

fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)
    val days = 256

//    val part1 = simulateLanternfish(input, days)
//    println("*** Result 1: $part1 ***")

    val part2 = calculateLanternfish(input, days)
    println("*** Result 2: $part2 ***")
}

fun simulateLanternfish(initialFish: List<Int>, days: Int): Int {
    val fishPopulation = initialFish.toMutableList()

    for (day in 0 until days) {
        println("Day $day: $fishPopulation")
        for (i in 0 until fishPopulation.size) {
            val currentTimer = fishPopulation[i]
            if (currentTimer == 0) {
                fishPopulation[i] = 6
                fishPopulation.add(8)
            } else {
                fishPopulation[i]--
            }
        }
    }

    return fishPopulation.size
}

fun calculateLanternfish(initialFish: List<Int>, days: Int): Long {
    var timerToCount = mutableMapOf<Int, Long>()
    initialFish.forEach {
        val currentCount = timerToCount.getOrDefault(it, 0)
        timerToCount[it] = currentCount + 1
    }
    println("Initial state: $timerToCount")

    for (day in 1..days) {
        val newTimerToCount = mutableMapOf<Int, Long>()
        timerToCount.forEach { (currentTimer, count) ->
            if (currentTimer == 0) {
                newTimerToCount[6] = newTimerToCount.getOrDefault(6, 0) + count
                newTimerToCount[8] = count
            } else if (currentTimer == 7) {
                newTimerToCount[6] = newTimerToCount.getOrDefault(6, 0) + count
            } else {
                newTimerToCount[currentTimer - 1] = count
            }
        }
        timerToCount = newTimerToCount

        println("After day $day: $timerToCount")
    }
    return timerToCount.values.sum()
}

private fun getInput(inputType: InputType): List<Int> {
    return File("src/main/kotlin/day_06/${inputType.fileName}.txt")
        .readLines()[0]
        .split(",")
        .map(String::toInt)
}
