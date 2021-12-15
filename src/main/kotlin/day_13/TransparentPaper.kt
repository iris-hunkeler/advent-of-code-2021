package day_13

data class TransparentPaper(
    var dotGrid: MutableList<MutableList<Boolean>> = mutableListOf(),
    val foldInstructions: List<FoldInstruction>
) {
    fun init(sizeX: Int, sizeY: Int) {
        dotGrid = createGrid(sizeX, sizeY)
    }

    private fun createGrid(sizeX: Int, sizeY: Int): MutableList<MutableList<Boolean>> {
        val newGrid = MutableList(sizeY) { mutableListOf<Boolean>() }
        for (i in 0 until sizeY) {
            newGrid[i] = MutableList(sizeX) { false }
        }
        return newGrid
    }

    fun countDots(): Int {
        var dotCount = 0

        dotGrid.forEachIndexed { y, line ->
            line.forEachIndexed { x, value ->
                if (value) {
                    dotCount++
                }
            }
        }
        return dotCount
    }

    fun executeAllRemainingFolds() {
        foldInstructions
            .filter { !it.done }
            .forEach {
                executeFold(it)
            }
    }

    fun executeNextFold() {
        foldInstructions.firstOrNull { !it.done }?.let { executeFold(it) }
    }

    private fun executeFold(instruction: FoldInstruction) {
        if (instruction.side == "y") {
            dotGrid = executeHorizontalFold(instruction.number)
            instruction.done = true
        } else if (instruction.side == "x") {
            dotGrid = executeVerticalFold(instruction.number)
            instruction.done = true
        } else {
            error("unknown FoldInstruction $instruction")
        }

        println("fold completed: $instruction")
    }

    private fun executeHorizontalFold(number: Int): MutableList<MutableList<Boolean>> {
        val newGrid = createGrid(dotGrid[0].size, number)

        newGrid.forEachIndexed { y, line ->
            val foldUpLine = 2 * number - y
            line.forEachIndexed { x, value ->
                if (foldUpLine < dotGrid.size) {
                    newGrid[y][x] = dotGrid[y][x] || dotGrid[foldUpLine][x]
                } else {
                    newGrid[y][x] = dotGrid[y][x]
                }
            }
        }

        return newGrid
    }

    private fun executeVerticalFold(number: Int): MutableList<MutableList<Boolean>> {
        val newGrid = createGrid(number, dotGrid.size)

        newGrid.forEachIndexed { y, line ->
            line.forEachIndexed { x, value ->
                val foldOverLine = 2 * number - x
                if (foldOverLine < dotGrid[0].size) {
                    newGrid[y][x] = dotGrid[y][x] || dotGrid[y][foldOverLine]
                } else {
                    newGrid[y][x] = dotGrid[y][x]
                }
            }
        }

        return newGrid
    }

    override fun toString(): String {
        var output = ""

        dotGrid.forEachIndexed { y, line ->
            line.forEachIndexed { x, value ->
                output = "$output${
                    if (value) {
                        "#"
                    } else {
                        "."
                    }
                }"
            }
            output = "$output\n"
        }

        output = "$output\n"
        foldInstructions.forEach {
            output = "$output$it\n"
        }

        return output
    }
}