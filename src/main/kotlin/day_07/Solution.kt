package day_07

import InputType
import java.io.File
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt

fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)

    val part1 = calculateFuelUsingConstantCost(input)
    println("*** Result 1: $part1 ***")

    val part2 = calculateFuelUsingIncreasingCost(input)
    println("*** Result 2: $part2 ***")
}

fun calculateFuelUsingConstantCost(horizontalPositions: List<Int>): Int {
    val median = horizontalPositions
        .sorted()[horizontalPositions.size/2]
    println("Move all crab submarines to $median")

    var fuelNeeded = 0
    horizontalPositions.forEach {
        fuelNeeded += abs(it - median)
    }

    return fuelNeeded
}

fun calculateFuelUsingIncreasingCost(horizontalPositions: List<Int>): Int {
    val averageFloor = horizontalPositions
        .sorted()
        .average()
        .toInt()

    val averageCeiling = horizontalPositions
        .sorted()
        .average()
        .roundToInt()

    println("Move all crab submarines to $averageFloor or $averageCeiling")

    var fuelToAverageFloor = 0
    var stepsToCost: MutableMap<Int, Int> = mutableMapOf()
    horizontalPositions.forEach {
        fuelToAverageFloor += calculateIncreasingCost(stepsToCost, abs(it - averageFloor))
    }

    var fuelToAverageCeiling = 0
    stepsToCost = mutableMapOf()
    horizontalPositions.forEach {
        fuelToAverageCeiling += calculateIncreasingCost(stepsToCost, abs(it - averageFloor))
    }

    return min(fuelToAverageFloor, fuelToAverageCeiling)
}

/* use recursion and dynamic programming to calculate costs based on costs for last step */
private fun calculateIncreasingCost(stepsToCost: MutableMap<Int, Int>, steps: Int): Int {
    if(steps == 0) {
        return 0
    }

    val cost = stepsToCost[steps - 1]
    if(cost == null) {
        stepsToCost[steps - 1] = calculateIncreasingCost(stepsToCost, steps - 1)
    }

    return stepsToCost[steps - 1]!! + steps
}

private fun getInput(inputType: InputType): List<Int> {
    return File("src/main/kotlin/day_07/${inputType.fileName}.txt")
        .readLines()[0]
        .split(",")
        .map(String::toInt)
}
