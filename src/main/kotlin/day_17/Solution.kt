package day_17

fun main(args: Array<String>) {
//    val input = TargetArea(20, 30, -10, -5)
    val input = TargetArea(195, 238, -93, -67)

    val (part1, part2) = simulateProbeToHitTargetArea(input)
    println("*** Result 1: $part1 ***")
    println("*** Result 2: $part2 ***")
}

fun simulateProbeToHitTargetArea(targetArea: TargetArea): Pair<Int, Int> {
    println("TargetArea: $targetArea")

    var bestX = 0
    var bestY = 0
    var maxYReachable = Int.MIN_VALUE
    var countOfHits = 0

    for (x in 0..1000) {
        for (y in -1000..1000) {
            val (hits, maxY) = targetArea.hits(x, y)
            if (hits) {
                countOfHits++
                println("Hit: $x,$y")

                if (maxY > maxYReachable) {
                    maxYReachable = maxY
                    bestX = x
                    bestY = y
                }
            }
        }
    }

    println("[to maximize Y] bestX = $bestX, bestY = $bestY")

    return Pair(maxYReachable, countOfHits)
}
