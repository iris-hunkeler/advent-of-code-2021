package day_12

import java.io.File

fun main(args: Array<String>) {
    val input = getInput("input")

    val part1 = countPossiblePaths(input)
    println("*** Result: $part1 ***")
}

fun countPossiblePaths(input: Map<String, List<String>>): Int {
    println("All edges:")
    println(input)

    val uniquePaths = mutableSetOf<String>()
    val path = mutableListOf<String>()
    path.add("start")

    continuePath(input, uniquePaths, path)

    println("uniquePaths: $uniquePaths")

    return uniquePaths.size
}

private fun continuePath(input: Map<String, List<String>>, uniquePaths: MutableSet<String>, currentPath: MutableList<String>) {
    val currentNode = currentPath.last()

    if (currentNode == "end") {
        uniquePaths.add(currentPath.toString())
    }

    val potentialNextNodes = (input[currentNode] ?: mutableListOf())
        .filter {
            it[0].isUpperCase() || !currentPath.contains(it)
                    || (additionalCheckForPart2(it, currentPath))
        }
//    println("path: $currentPath -> potentialNextNodes for $currentNode: $potentialNextNodes")

    potentialNextNodes.forEach {
        val newPath = currentPath.toMutableList()
        newPath.add(it)
        continuePath(input, uniquePaths, newPath)
    }
}

private fun additionalCheckForPart2(it: String, currentPath: MutableList<String>) =
    it != "start" && it != "end" && canStillVisitAnySmallCaveTwice(currentPath)

private fun canStillVisitAnySmallCaveTwice(currentPath: List<String>): Boolean {
    val visitedSmallCaves = mutableSetOf<String>()
    for(cave in currentPath)  {
        if(cave[0].isLowerCase() && cave != "start" && cave != "end" ) {
            val newVisit = visitedSmallCaves.add(cave)
            if(!newVisit) {
                return false
            }
        }
    }
    return true
}

private fun getInput(fileName: String): Map<String, List<String>> {
    val edges = mutableMapOf<String, MutableList<String>>()
    File("src/main/kotlin/day_12/$fileName.txt")
        .readLines()
        .map {
            val (from, to) = it.split("-")
            val existingTargets = edges.getOrDefault(from, mutableListOf())
            existingTargets.add(to)
            edges[from] = existingTargets

            val existingTargetsFlipped = edges.getOrDefault(to, mutableListOf())
            existingTargetsFlipped.add(from)
            edges[to] = existingTargetsFlipped
        }

    return edges
}

