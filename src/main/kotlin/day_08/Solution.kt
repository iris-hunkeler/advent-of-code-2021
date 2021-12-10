package day_08

import InputType
import java.io.File

fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)

    val part1 = countOutputValuesWithUniqueNumberOfSegments(input)
    println("*** Result 1: $part1 ***")

    val part2 = sumOutputValues(input)
    println("*** Result 2: $part2 ***")
}

fun countOutputValuesWithUniqueNumberOfSegments(signals: List<Entry>): Int {
    var count = 0
    signals.forEach { entry ->
        count += entry.outputValues.count {
            it.size == 2 || it.size == 3 || it.size == 4 || it.size == 7
        }
    }

    return count
}

fun sumOutputValues(signals: List<Entry>): Int {
    var sum = 0
    signals.forEach { entry ->
        val signalStringToNumber = extractSignalStringToNumber(entry)
        val outputNumber = extractOutputNumber(entry, signalStringToNumber)
        sum += outputNumber
    }

    return sum
}

private fun extractOutputNumber(entry: Entry, signalStringToNumber: Map<String, Int>): Int {
    var outputNumber = ""
    entry.outputValues.forEach { outputSignal ->
        outputNumber = "${outputNumber}${signalStringToNumber[outputSignal.sorted().toString()]}"
    }
    return outputNumber.toInt()
}

private fun extractSignalStringToNumber(entry: Entry): Map<String, Int> {
    val numberToSignal: MutableMap<Int, List<Char>> = mutableMapOf()

    numberToSignal[1] = entry.signalPatterns.find { it.size == 2 }!!
    numberToSignal[4] = entry.signalPatterns.find { it.size == 4 }!!
    numberToSignal[7] = entry.signalPatterns.find { it.size == 3 }!!
    numberToSignal[8] = entry.signalPatterns.find { it.size == 7 }!!

    numberToSignal[6] = extractNumber6(entry, numberToSignal)
    val topRight = extractTopRight(numberToSignal)
    numberToSignal[3] = extractNumber3(entry, numberToSignal)
    numberToSignal[9] = extractNumber9(entry, numberToSignal)
    val bottomLeft = extractBottomLeft(numberToSignal)
    numberToSignal[2] = extractNumber2(entry, topRight, bottomLeft)
    numberToSignal[5] = extractNumber5(entry, numberToSignal)
    numberToSignal[0] = extractNumber0(entry, numberToSignal)

    val signalStringToNumber = numberToSignal.map {
        it.value.sorted().toString() to it.key
    }.toMap()
    println(signalStringToNumber)
    return signalStringToNumber
}


private fun extractNumber6(entry: Entry, numberToSignal: MutableMap<Int, List<Char>>) = entry.signalPatterns.find {
    it.size == 6 && !it.containsAll(numberToSignal[1]!!)
}!!.toList()

private fun extractNumber3(entry: Entry, numberToSignal: MutableMap<Int, List<Char>>) = entry.signalPatterns.find {
    it.size == 5 && it.containsAll(numberToSignal[7]!!)
}!!.toList()

private fun extractNumber9(entry: Entry, numberToSignal: MutableMap<Int, List<Char>>) = entry.signalPatterns.find {
    it.size == 6 && it.containsAll(numberToSignal[3]!!)
}!!.toList()

private fun extractNumber2(entry: Entry, topRight: Char, bottomLeft: Char) = entry.signalPatterns.find {
    it.size == 5 && it.contains(topRight) && it.contains(bottomLeft)
}!!.toList()

private fun extractNumber5(entry: Entry, numberToSignal: MutableMap<Int, List<Char>>) = entry.signalPatterns.find {
    it.size == 5 && listContentsAreDifferent(it, numberToSignal[2]!!) && listContentsAreDifferent(it, numberToSignal[3]!!)
}!!.toList()

private fun extractNumber0(entry: Entry, numberToSignal: MutableMap<Int, List<Char>>) = entry.signalPatterns.find {
    it.size == 6 && listContentsAreDifferent(it, numberToSignal[9]!!) && listContentsAreDifferent(it, numberToSignal[6]!!)
}!!.toList()

fun extractTopRight(numberToSignal: MutableMap<Int, List<Char>>): Char {
    val segments = numberToSignal[1]!!.toMutableList()
    segments.removeAll(numberToSignal[6]!!)
    return segments.first()
}

private fun extractBottomLeft(numberToSignal: MutableMap<Int, List<Char>>): Char {
    val segments = numberToSignal[8]!!.toMutableList()
    segments.removeAll(numberToSignal[9]!!)
    return segments.first()
}

private fun listContentsAreEqual(listA: List<Char>, listB: List<Char>) =
    (listA.containsAll(listB) && listB.containsAll(listA))

private fun listContentsAreDifferent(listA: List<Char>, listB: List<Char>) =
    !(listContentsAreEqual(listA, listB))

private fun getInput(inputType: InputType): List<Entry> {
    return File("src/main/kotlin/day_08/${inputType.fileName}.txt")
        .readLines()
        .map {
            val (signalPatterns, outputValues) = it.split(" | ")
            Entry(
                signalPatterns = signalPatterns.split(" ").map { it.toList() },
                outputValues = outputValues.split(" ").map { it.toList() }
            )
        }
}

data class Entry(
    val signalPatterns: List<List<Char>>, // always size 10
    val outputValues: List<List<Char>> // always size 4
)

