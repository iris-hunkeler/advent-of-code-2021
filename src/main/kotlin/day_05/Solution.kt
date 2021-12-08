package day_05

import InputType
import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs

fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)

    val part1 = countDangerousAreas(input)
    println("*** Result 1: $part1 ***")
}

fun countDangerousAreas(lines: List<Line>): Int {
//    println(lines)
    var max = 0
    lines.forEach { line ->
        max = maxOf(max, line.x1, line.x2, line.y1, line.y2)
    }

    val floor = Floor()
    floor.init(max + 1)
    lines.forEach {
        floor.addLine(it)
    }

//    floor.print()

    return floor.countDangerZones()
}

private fun getInput(inputType: InputType): List<Line> {
    return File("src/main/kotlin/day_05/${inputType.fileName}.txt")
        .readLines()
        .map {
            val points = it.split(" -> ")
            Line(
                x1 = points[0].split(",")[0].toInt(),
                y1 = points[0].split(",")[1].toInt(),
                x2 = points[1].split(",")[0].toInt(),
                y2 = points[1].split(",")[1].toInt(),
            )
        }
}

data class Line(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int
) {
    fun isHorizontal(): Boolean {
        return y1 == y2
    }

    fun isVertical(): Boolean {
        return x1 == x2
    }

    fun isDiagonal(): Boolean {
        return abs(x1 - x2) == abs(y1 - y2)
    }
}

data class Floor(
    var grid: MutableList<MutableList<Int>> = mutableListOf()
) {
    fun init(size: Int) {
        grid = MutableList(size) { mutableListOf() }
        for (i in 0 until size) {
            grid[i] = MutableList(size) { 0 }
        }
    }

    fun print() {
        for (row in grid) {
            for (point in row) {
                print(
                    if (point == 0) {
                        "."
                    } else {
                        point.toString()
                    }
                )
            }
            println()
        }
    }

    fun addLine(line: Line) {
        if (line.isHorizontal()) {
            for (i in min(line.x1, line.x2)..max(line.x1, line.x2)) {
                grid[line.y1][i]++
            }
        } else if (line.isVertical()) {
            for (i in min(line.y1, line.y2)..max(line.y1, line.y2)) {
                grid[i][line.x1]++
            }
        } else if(line.isDiagonal()){
            var currentX = line.x1
            var currentY = line.y1
            for(i in 0 .. abs(line.x1 - line.x2)) {
                grid[currentY][currentX]++

                if(line.x1 < line.x2) {
                    currentX++
                } else {
                    currentX--
                }

                if(line.y1 < line.y2) {
                    currentY++
                } else {
                    currentY--
                }
            }
        } else {
            error("Unknown line type: $line")
        }
    }

    fun countDangerZones(): Int {
        var count = 0

        for (row in grid) {
            for (point in row) {
               if(point > 1) {
                   count ++
               }
            }
        }

        return count
    }
}