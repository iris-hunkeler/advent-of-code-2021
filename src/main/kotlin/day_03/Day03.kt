package day_03

import InputType
import java.io.File

fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)

    val part1 = calculatePowerConsumption(input)
    println("*** Part 1: $part1 ***")

    val part2 = calculateLifeSupport(input)
    println("*** Part 2: $part2 ***")
}

fun calculatePowerConsumption(input: List<String>): Int {
    val trackMostCommonBit = mutableListOf<Int>()
    for (i in 0 until input[0].length) {
        trackMostCommonBit.add(getMostCommonBitForPosition(input, i))
    }

    var gamma = ""
    var epsilon = ""
    trackMostCommonBit.forEach {
        when (it) {
            1 -> {
                gamma += "1"
                epsilon += "0"
            }
            0 -> {
                gamma += "0"
                epsilon += "1"
            }
            else -> error("Unknown bit $it")
        }
    }

    val gammaDecimal = gamma.toInt(2)
    val epsilonDecimal = epsilon.toInt(2)

    println("gamma: binary = $gamma, decimal = $gammaDecimal")
    println("epsilon: binary = $epsilon, decimal = $epsilonDecimal")

    return gammaDecimal * epsilonDecimal
}

fun calculateLifeSupport(input: List<String>): Int {
    val oxygen = extractRating(input, true)
    val co2 = extractRating(input, false)

    val oxygenDecimal = oxygen.toInt(2)
    val co2Decimal = co2.toInt(2)

    println("oxygen: binary = $oxygen, decimal = $oxygenDecimal")
    println("c02: binary = $co2, decimal = $co2Decimal")

    return oxygenDecimal * co2Decimal
}

fun extractRating(input: List<String>, keepIdenticals: Boolean): String {
    var oxygenPotentialMatches = input.toMutableList()
    for (i in 0 until input[0].length) {
        val bit = getMostCommonBitForPosition(oxygenPotentialMatches, i)
        oxygenPotentialMatches = oxygenPotentialMatches
            .filter {
                if (keepIdenticals) {
                    it[i].digitToInt() == bit
                } else {

                    it[i].digitToInt() != bit
                }
            }
            .toMutableList()
        if (oxygenPotentialMatches.size == 1) {
            return oxygenPotentialMatches[0]
        }
    }
    error("No result found.")
}

fun getMostCommonBitForPosition(input: List<String>, position: Int): Int {
    var trackMostCommonBit = 0
    input.forEach { line ->
        trackMostCommonBit = when (line[position]) {
            '1' -> trackMostCommonBit + 1
            '0' -> trackMostCommonBit - 1
            else -> error("Not supported character: $line[position]")
        }
    }

    return if (trackMostCommonBit > 0) {
        1
    } else if (trackMostCommonBit < 0) {
        0
    } else {
        // default
        1
    }
}

private fun getInput(inputType: InputType): List<String> {
    return File("src/main/kotlin/day_03/${inputType.fileName}.txt")
        .readLines()
}
