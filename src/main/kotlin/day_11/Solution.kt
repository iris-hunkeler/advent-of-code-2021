package day_11

import InputType
import java.io.File

fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)

    val part1 = countTotalFlashes(input.copy(), 100)
    println("*** Result 1: $part1 ***")

    val part2 = findSynchronizedFlashStep(input.copy(), 500)
    println("*** Result 2: $part2 ***")
}

fun countTotalFlashes(input: Grid, steps: Int): Int {
    println("Before any steps:")
    println(input)

    var flashCount = 0
    for (i in 1..steps) {
        val setOfFlashed = executeStep(i, input)
        flashCount += setOfFlashed.size
    }

    return flashCount
}

fun findSynchronizedFlashStep(input: Grid, steps: Int): Int {
    println("Before any steps:")
    println(input)

    for (i in 1..steps) {
        val setOfFlashed = executeStep(i, input)
        if(setOfFlashed.size == 100) {
            return i
        }
    }

    error("No synchronized flash found after $steps steps")
}

private fun executeStep(i: Int, input: Grid): MutableSet<Pair<Int, Int>> {
    val pointsToFlash = ArrayDeque<Point>()

    increaseAllEnergyLevels(input, pointsToFlash)

    val setOfFlashed = mutableSetOf<Pair<Int, Int>>()
    while (pointsToFlash.isNotEmpty()) {
        val point = pointsToFlash.removeLast()
        flashPoint(setOfFlashed, point, input, pointsToFlash)
    }

    println("After Step $i:")
    println(input)
    return setOfFlashed
}

private fun increaseAllEnergyLevels(input: Grid, pointsToFlash: ArrayDeque<Point>) {
    input.map.forEachIndexed { x, line ->
        line.forEachIndexed { y, value ->
            val point = input.getPoint(x, y)
            increaseEnergy(input, point, pointsToFlash)
        }
    }
}

private fun flashPoint(
    setOfFlashed: MutableSet<Pair<Int, Int>>,
    point: Point,
    input: Grid,
    pointsToFlash: ArrayDeque<Point>
) {
    if (setOfFlashed.add(Pair(point.x, point.y))) {
        input.map[point.x][point.y] = 0
        input.getAllAdjacentPoints(point).forEach { pointToIncreaseEnergy ->
            if (!setOfFlashed.contains(Pair(pointToIncreaseEnergy.x, pointToIncreaseEnergy.y))) {
                increaseEnergy(input, pointToIncreaseEnergy, pointsToFlash)
            }
        }
    }
}

private fun increaseEnergy(input: Grid, point: Point, pointsToFlash: ArrayDeque<Point>) {
    input.map[point.x][point.y] += 1

    if (input.map[point.x][point.y] > 9) {
        pointsToFlash.add(point)
    }
}

private fun getInput(inputType: InputType): Grid {
    return Grid(
        File("src/main/kotlin/day_11/${inputType.fileName}.txt")
            .readLines()
            .map {
                it.chunked(1).map { it.toInt() }.toMutableList()
            }.toMutableList()
    )
}

