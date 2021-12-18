package day_15

import InputType
import java.io.File
import java.util.*

private const val INCREASE_MAP = 5

fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)

    val part1 = calculateLowestRiskPath(input)
    println("*** Result 1: $part1 ***")

    val part2 = calculateLowestRiskPathOnLargerMap(input)
    println("*** Result 2: $part2 ***")
}

fun calculateLowestRiskPath(map: RiskLevelMap): Int {
//    println(map)

    val nodeToRiskHeapUnprocessed = PriorityQueue<Pair<String, Int>>(compareBy { it.second })
    val nodeToRisk = mutableMapOf<String, Int>()

    val nodeToParent = mutableMapOf<String, String>()
    val processedNodes = mutableSetOf<String>()

    map.getAllNeighbors(map.getStartNode()).forEach {
        nodeToRiskHeapUnprocessed.add(Pair(it, map.getRisk(it)))
        nodeToRisk[it] = map.getRisk(it)
        nodeToParent[it] = map.getStartNode()
    }

    var start = System.nanoTime()
    while (nodeToRiskHeapUnprocessed.isNotEmpty()) {
        val (currentNode, riskCurrentNode) = nodeToRiskHeapUnprocessed.remove()

        map.getAllNeighbors(currentNode).forEach { neighbor ->
            val riskNeighbor = nodeToRisk[neighbor] ?: Int.MAX_VALUE

            if (riskCurrentNode + map.getRisk(neighbor) < riskNeighbor) {
//                println("lower risk path to neighbor $neighbor found")
                nodeToRisk[neighbor] = riskCurrentNode + map.getRisk(neighbor)
                nodeToRiskHeapUnprocessed.add(Pair(neighbor, riskCurrentNode + map.getRisk(neighbor)))
                nodeToParent[neighbor] = currentNode
            }
        }

        processedNodes.add(currentNode)
        nodeToRiskHeapUnprocessed.removeIf { pair -> pair.first == currentNode }

        if (processedNodes.size % 5000 == 0) {
            println("processed ${processedNodes.size} nodes out of ${map.getSizeX() * map.getSizeY()} - ${(System.nanoTime() - start) / 1_000_000} ms ")
            start = System.nanoTime()
        }
    }

    return nodeToRisk[map.getEndNode()]!!
}

fun calculateLowestRiskPathOnLargerMap(map: RiskLevelMap): Int {
    val newRiskLevelMap = buildLargerMap(map)

    return calculateLowestRiskPath(newRiskLevelMap)
}

private fun buildLargerMap(map: RiskLevelMap): RiskLevelMap {
    val largerMap = MutableList(map.getSizeY() * INCREASE_MAP) { mutableListOf<Int>() }
    for (i in 0 until largerMap.size) {
        largerMap[i] = MutableList(map.getSizeX() * INCREASE_MAP) { 0 }
    }

    val riskIncreaseControl = MutableList(INCREASE_MAP) { mutableListOf<Int>() }
    for (i in 0 until riskIncreaseControl.size) {
        riskIncreaseControl[i] = MutableList(INCREASE_MAP) { 0 }
    }

    for (stepX in 0 until INCREASE_MAP) {
        for (stepY in 0 until INCREASE_MAP) {

            val riskIncreaseStep = stepX + stepY
            riskIncreaseControl[stepY][stepX] = riskIncreaseStep

            for (x in 0 until map.getSizeX()) {
                for (y in 0 until map.getSizeY()) {
                    val originalRisk = map.getRisk(RiskLevelMap.encodeNode(x, y))

                    var newRisk = originalRisk + riskIncreaseStep
                    if (newRisk > 9) {
                        newRisk -= 9
                    }

                    largerMap[y + (stepY * map.getSizeY())][x + (stepX * map.getSizeX())] = newRisk
                }
            }
        }
    }

//    println(RiskLevelMap(riskIncreaseControl))

    return RiskLevelMap(largerMap)
}

@Deprecated("Slows down for large maps due to the growing map")
private fun findLowestRiskUnprocessedNode(nodeToRisk: Map<String, Int>, processedNodes: Set<String>): String? {
    var lowestRisk = Int.MAX_VALUE
    var lowestRiskNode: String? = null

    nodeToRisk.keys.minus(processedNodes).forEach { nodeKey ->
        val riskOfCurrentNode = nodeToRisk[nodeKey]!!
        if (riskOfCurrentNode < lowestRisk) {
            lowestRisk = riskOfCurrentNode
            lowestRiskNode = nodeKey
        }
    }
    return lowestRiskNode
}

private fun getInput(inputType: InputType): RiskLevelMap {
    return RiskLevelMap(File("src/main/kotlin/day_15/${inputType.fileName}.txt")
        .readLines()
        .map { line ->
            line.chunked(1).map { it.toInt() }
        }
    )
}
