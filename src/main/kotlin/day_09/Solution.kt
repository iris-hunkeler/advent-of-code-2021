package day_09

import InputType
import java.io.File

fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)

    val part1 = calculateRiskLevel(input)
    println("*** Result 1: $part1 ***")

    val part2 = findThreeLargestBasinValue(input)
    println("*** Result 2: $part2 ***")
}

fun calculateRiskLevel(map: List<List<Int>>): Int {
    var riskLevel = 0

    map.forEachIndexed { x, line ->
        line.forEachIndexed { y, value ->
            val point = Point(x, y, value)
            if (isLowPoint(map, point)) {
                riskLevel += 1 + point.value
            }
        }
    }

    return riskLevel
}


fun findThreeLargestBasinValue(map: List<List<Int>>): Int {
    val basinSizeList = mutableListOf<Int>()

    map.forEachIndexed { x, line ->
        line.forEachIndexed { y, value ->
            val point = Point(x, y, value)
            if (isLowPoint(map, point)) {
                val pointsInBasin = mutableSetOf<Point>()
                val pointsToCheck = ArrayDeque<Point>()
                pointsToCheck.add(point)

                while (pointsToCheck.isNotEmpty()) {
                    val pointToCheck = pointsToCheck.removeFirst()
                    if(pointToCheck.value < 9) {
                        if (pointsInBasin.add(pointToCheck)) {
                            getUpperPoint(map, pointToCheck)?.let { pointsToCheck.add(it) }
                            getLeftPoint(map, pointToCheck)?.let { pointsToCheck.add(it) }
                            getRightPoint(map, pointToCheck)?.let { pointsToCheck.add(it) }
                            getLowerPoint(map, pointToCheck)?.let { pointsToCheck.add(it) }
                        }
                    }
                }
                basinSizeList.add(pointsInBasin.size)
//                println("low point $point => basin size = ${pointsInBasin.size} => basin = $pointsInBasin")
            }
        }
    }

    val potentiallyLargestBasinsListSorted = basinSizeList.sortedDescending()
//    println("Sorted basin size: $potentiallyLargestBasinsListSorted")

    return potentiallyLargestBasinsListSorted[0] * potentiallyLargestBasinsListSorted[1] * potentiallyLargestBasinsListSorted[2]
}

data class Point(
    val x: Int,
    val y: Int,
    val value: Int
)

private fun isLowPoint(map: List<List<Int>>, point: Point): Boolean {
    if ((getUpperPoint(map, point)?.value ?: 9) <= point.value) {
        return false
    } else if ((getLeftPoint(map, point)?.value ?: 9) <= point.value) {
        return false
    } else if ((getLowerPoint(map, point)?.value ?: 9) <= point.value) {
        return false
    } else if ((getRightPoint(map, point)?.value ?: 9) <= point.value) {
        return false
    }
    return true
}

private fun getUpperPoint(map: List<List<Int>>, point: Point) = if (point.x > 0) {
    Point(point.x - 1, point.y, map[point.x - 1][point.y])
} else {
    null
}

private fun getLeftPoint(map: List<List<Int>>, point: Point) = if (point.y > 0) {
    Point(point.x, point.y - 1, map[point.x][point.y - 1])
} else {
    null
}

private fun getLowerPoint(map: List<List<Int>>, point: Point) = if (point.x < map.size - 1) {
    Point(point.x + 1, point.y, map[point.x + 1][point.y])
} else {
    null
}

private fun getRightPoint(map: List<List<Int>>, point: Point) = if (point.y < map[0].size - 1) {
    Point(point.x, point.y + 1, map[point.x][point.y + 1])
} else {
    null
}

private fun getInput(inputType: InputType): List<List<Int>> {
    return File("src/main/kotlin/day_09/${inputType.fileName}.txt")
        .readLines()
        .map {
            it.chunked(1).map { it.toInt() }
        }
}
