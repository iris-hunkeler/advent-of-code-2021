package day_14

import InputType
import java.io.File

fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)

    val part1 = simulatePolymerGrowth(input.copy(), 10)
    println("*** Result 1: $part1 ***")

    val part2 = simulatePolymerGrowthUsingDynamicProgramming(input.copy(), 40)
    println("*** Result 2: $part2 ***")
}

fun simulatePolymerGrowth(polymer: GrowablePolymer, steps: Int): Long {
    println("Template: ${polymer.currentPolymer}")

    for (i in 1..steps) {
        val newPolymer = growPolymerOneStep(polymer.currentPolymer, polymer.growthInstructions)
        polymer.currentPolymer = newPolymer

//        println("After step $i: ${polymer.currentPolymer}")
    }

    val elementToCount = countElementOccurrences(polymer.currentPolymer)
    val maxValue = elementToCount.maxByOrNull { it.value }!!.value
    val minValue = elementToCount.minByOrNull { it.value }!!.value

    return maxValue - minValue
}

fun simulatePolymerGrowthUsingDynamicProgramming(polymer: GrowablePolymer, steps: Int): Long {
    val cache = mutableMapOf<String, Map<Char, Long>>()

    val elementToCount = simulatePolymerGrowthUsingDynamicProgramming(polymer.currentPolymer, polymer.growthInstructions, steps, cache)

    val maxValue = elementToCount.maxByOrNull { it.value }!!.value
    val minValue = elementToCount.minByOrNull { it.value }!!.value

    return maxValue - minValue
}

fun simulatePolymerGrowthUsingDynamicProgramming(
    polymer: String,
    growthInstructions: Map<String, String>,
    steps: Int,
    cache: MutableMap<String, Map<Char, Long>>
): Map<Char, Long> {
    if (cache.containsKey(getCacheKey(polymer, steps))) {
        return cache[getCacheKey(polymer, steps)]!!
    }

    var elementToCount = mutableMapOf<Char, Long>()

    if (steps == 0) {
        elementToCount = countElementOccurrences(polymer).toMutableMap()
//        println("BASE CASE: $polymer -> $elementToCount")
    } else {

        polymer.forEachIndexed { i, _ ->
            if (i != polymer.length - 1) {
                val currentPair = polymer.substring(i, i + 2)
                val currentPolymer = growPolymerOneStep(currentPair, growthInstructions)

                val elementToCountForPair = simulatePolymerGrowthUsingDynamicProgramming(currentPolymer, growthInstructions, steps - 1, cache)

                elementToCount = sumUpCountsForElements(elementToCount, elementToCountForPair)

                if (i != polymer.length - 2) {
                    // remove duplicate count of overlapping pairs
                    elementToCount[currentPair[1]] = elementToCount[currentPair[1]]!! - 1
                }
            } else {
//                println("polymer = $polymer, steps = $steps, elementToCount = $elementToCount")
            }
        }
    }

    cache[getCacheKey(polymer, steps)] = elementToCount
    return elementToCount
}

private fun sumUpCountsForElements(
    elementToCount: Map<Char, Long>,
    elementToCountForPair: Map<Char, Long>
): MutableMap<Char, Long> {
    return (elementToCount.keys + elementToCountForPair.keys)
        .associateWith { elementToCount.getOrDefault(it, 0) + elementToCountForPair.getOrDefault(it, 0) }
        .toMutableMap()
}

private fun getCacheKey(polymer: String, steps: Int) = "$polymer-$steps"

private fun growPolymerOneStep(polymer: String, growthInstructions: Map<String, String>): String {
    val builder = StringBuilder()

    polymer.forEachIndexed { i, char ->
        builder.append(char)

        if (i != polymer.length - 1) {
            val currentPair = polymer.substring(i, i + 2)
            growthInstructions[currentPair]?.let {
                builder.append(it)
            }
        }
    }

    return builder.toString()
}

private fun countElementOccurrences(polymer: String): Map<Char, Long> {
    val elementToCount = mutableMapOf<Char, Long>()

    polymer.forEach { element ->
        val currentCount = elementToCount.getOrDefault(element, 0)
        elementToCount[element] = currentCount + 1
    }

    return elementToCount
}

private fun getInput(inputType: InputType): GrowablePolymer {
    val originalPolymer = File("src/main/kotlin/day_14/${inputType.fileName}.txt")
        .readLines()
        .first()


    val growthInstructions = File("src/main/kotlin/day_14/${inputType.fileName}.txt")
        .readLines()
        .filter { it.contains("->") }
        .associate {
            val (pair, insertionElement) = it.split(" -> ")
            Pair(pair, insertionElement)
        }

    return GrowablePolymer(
        originalPolymer,
        growthInstructions,
    )
}

