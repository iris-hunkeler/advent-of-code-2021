package day_20

import InputType
import java.io.File

fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)

//    val part1 = processImage(input, 2)
//    println("*** Result 1: $part1 ***")

    val part2 = processImage(input, 50)
    println("*** Result 2: $part2 ***")
}

fun processImage(image: Image, numberOfRounds: Int): Int {
    println("Original \n$image")

    for (i in 1..numberOfRounds) {
        padAndEnhance(image, i)
    }

    return image.countLitPixels()
}

private fun padAndEnhance(image: Image, iteration: Int) {
    image.addPadding(2)
//    println("Padded (round $iteration)\n$image")
    image.enhance()
//    println("Enhanced (round $iteration)\n$image")

    println("Padded and enhanced $iteration")
}

private fun getInput(inputType: InputType): Image {
    return Image(
        enhancementAlgorithm = File("src/main/kotlin/day_20/${inputType.fileName}.txt")
            .readLines()
            .first()
            .chunked(1).map { it[0] },
        inputImage = File("src/main/kotlin/day_20/${inputType.fileName}.txt")
            .readLines()
            .mapIndexedNotNull { index, line ->
                if (index > 1) {
                    line.chunked(1).map { it[0] }
                } else {
                    null
                }
            }
    )
}
