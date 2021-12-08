package day_04

import InputType
import java.io.File

fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)

    val part1 = playBingoWithWinningStrategy(input)
    println("*** Part 1: $part1 ***")

    val part2 = playBingoWithLosingStrategy(input)
    println("*** Part 2: $part2 ***")
}

fun playBingoWithWinningStrategy(input: BingoGame): Int {
    for(i in 1 until input.numbers.size) {
        val currentlyDrawnNumbers = input.numbers.subList(0, i)
        input.boards.forEachIndexed { index, board ->
            if(board.isWinner(currentlyDrawnNumbers)) {
                println("Winner: Board $index")
                return board.getScore(currentlyDrawnNumbers)
            }
        }
    }

    return 0
}


fun playBingoWithLosingStrategy(input: BingoGame): Int {
    val winningBoards = mutableSetOf<Int>()

    for(i in 1 until input.numbers.size) {
        val currentlyDrawnNumbers = input.numbers.subList(0, i)
        input.boards.forEachIndexed { index, board ->
            if(board.isWinner(currentlyDrawnNumbers)) {
                winningBoards.add(index)

                if(winningBoards.size == input.boards.size) {
                    println("Last Winner: Board $index")
                    return board.getScore(currentlyDrawnNumbers)
                }
            }
        }
    }

    return 0
}


private fun getInput(inputType: InputType): BingoGame {
    val lines = File("src/main/kotlin/day_04/${inputType.fileName}.txt")
        .readLines()

    val drawnNumbers = lines[0]
        .split(",")
        .map(String::toInt)

    val listOfBingoBoards = mutableListOf<BingoBoard>()
    var bingoBoard = mutableListOf<List<Int>>()
    for (line in lines.subList(2, lines.size)) {
        if (line.isNotEmpty()) {
            val oneLine = line
                .trim()
                .replace("  ", " ")
                .split(" ")
                .map(String::toInt)
                .toList()

            bingoBoard.add(oneLine)

            if (bingoBoard.size == 5) {
                listOfBingoBoards.add(BingoBoard(bingoBoard))
                bingoBoard = mutableListOf()
            }
        }
    }


    return BingoGame(
        boards = listOfBingoBoards,
        numbers = drawnNumbers
    )
}

data class BingoGame(
    val boards: List<BingoBoard>,
    val numbers: List<Int>
)

data class BingoBoard(
    val board: List<List<Int>>
) {
    fun isWinner(numbers: List<Int>): Boolean {
        return hasWinningRow(numbers) || hasWinningColumn(numbers)
    }

    private fun hasWinningRow(numbers: List<Int>): Boolean {
       board.forEach {
           if (numbers.containsAll(it)) {
                return true
            }
        }

        return false
    }

    private fun hasWinningColumn(numbers: List<Int>): Boolean {
        for(i in 0 until board[0].size) {
            val column = listOf(board[0][i], board[1][i], board[2][i],board[3][i], board[4][i])
            if(numbers.containsAll(column)) {
                return true
            }
        }
        return false
    }

    fun getScore(numbers: List<Int>): Int {
        val allNumbers = board
            .flatten()
            .toMutableList()
        allNumbers.removeAll(numbers)

        return allNumbers.sum() * numbers.last()
    }
}