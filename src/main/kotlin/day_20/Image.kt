package day_20

data class Image(
    private val enhancementAlgorithm: List<Char>,
    private var inputImage: List<List<Char>>,
    private var surroundingPixel: Char = DARK
) {
    companion object {
        const val LIT = '#'
        const val DARK = '.'

        fun encodeNode(x: Int, y: Int) = "$x-$y"

        fun decodeNode(node: String) = Pair(node.split("-")[0].toInt(), node.split("-")[1].toInt())
    }

    private fun getSizeX() = inputImage[0].size
    private fun getSizeY() = inputImage.size

    private fun getPixel(node: String?): Char? {
        if (node == null) {
            return null
        }
        val (x, y) = decodeNode(node)
        return inputImage[y][x]
    }


    fun addPadding(paddingSize: Int) {
        val newSizeY = getSizeY() + (paddingSize * 2)
        val newSizeX = getSizeX() + (paddingSize * 2)
        val paddedImage = MutableList(newSizeY) { mutableListOf<Char>() }

        for (y in 0 until newSizeY) {
            paddedImage[y] = MutableList(newSizeX) { surroundingPixel }
            for (x in 0 until newSizeX) {
                if (x >= paddingSize && x < newSizeX - paddingSize && y >= paddingSize && y < newSizeY - paddingSize) {
                    paddedImage[y][x] = inputImage[y - paddingSize][x - paddingSize]
                }
            }
        }
        inputImage = paddedImage
    }

    fun enhance() {
        val enhancedImage = MutableList(getSizeY()) { mutableListOf<Char>() }

        for (y in 0 until getSizeY()) {
            enhancedImage[y] = MutableList(getSizeX()) { surroundingPixel }
            for (x in 0 until getSizeX()) {
                val neighborString = getNeighborString(encodeNode(x, y))
                val index = transformToDecimal(neighborString)
                enhancedImage[y][x] = enhancementAlgorithm[index]
            }
        }

        if(enhancementAlgorithm[0] == LIT) {
            if(surroundingPixel == DARK) {
                surroundingPixel = LIT
            } else {
                surroundingPixel = DARK
            }
        }

        inputImage = enhancedImage
    }

    fun countLitPixels(): Int {
        var count = 0
        inputImage.forEach { line ->
            line.forEach { value ->
                if (value == LIT) {
                    count++
                }
            }
        }

        return count
    }

    private fun transformToDecimal(neighborString: String): Int {
        return Integer.parseInt(neighborString.replace(LIT, '1').replace(DARK, '0'), 2)
    }

    private fun getNeighborString(node: String): String {
        val environmentString = StringBuilder()

        val (x, y) = decodeNode(node)

        val fallback = surroundingPixel

        environmentString.append(getPixel(getUpperLeftNeighbor(x, y)) ?: fallback)
        environmentString.append(getPixel(getUpperNeighbor(x, y)) ?: fallback)
        environmentString.append(getPixel(getUpperRightNeighbor(x, y)) ?: fallback)

        environmentString.append(getPixel(getLeftNeighbor(x, y)) ?: fallback)
        environmentString.append(getPixel(node) ?: fallback)
        environmentString.append(getPixel(getRightNeighbor(x, y)) ?: fallback)

        environmentString.append(getPixel(getLowerLeftNeighbor(x, y)) ?: fallback)
        environmentString.append(getPixel(getLowerNeighbor(x, y)) ?: fallback)
        environmentString.append(getPixel(getLowerRightNeighbor(x, y)) ?: fallback)

        return environmentString.toString()
    }

    private fun getUpperNeighbor(x: Int, y: Int) = if (y > 0) {
        encodeNode(x, y - 1)
    } else {
        null
    }

    private fun getUpperRightNeighbor(x: Int, y: Int) = if (y > 0 && x < inputImage[0].size - 1) {
        encodeNode(x + 1, y - 1)
    } else {
        null
    }

    private fun getRightNeighbor(x: Int, y: Int) = if (x < inputImage[0].size - 1) {
        encodeNode(x + 1, y)
    } else {
        null
    }

    private fun getLowerRightNeighbor(x: Int, y: Int) = if (y < inputImage.size - 1 && x < inputImage[0].size - 1) {
        encodeNode(x + 1, y + 1)
    } else {
        null
    }

    private fun getLowerNeighbor(x: Int, y: Int) = if (y < inputImage.size - 1) {
        encodeNode(x, y + 1)
    } else {
        null
    }

    private fun getLowerLeftNeighbor(x: Int, y: Int) = if (y < inputImage.size - 1 && x > 0) {
        encodeNode(x - 1, y + 1)
    } else {
        null
    }

    private fun getLeftNeighbor(x: Int, y: Int) = if (x > 0) {
        encodeNode(x - 1, y)
    } else {
        null
    }

    private fun getUpperLeftNeighbor(x: Int, y: Int) = if (x > 0 && y > 0) {
        encodeNode(x - 1, y - 1)
    } else {
        null
    }

    override fun toString(): String {
        var output = ""

        inputImage.forEach { line ->
            line.forEach { value ->
                output = "$output$value"
            }
            output = "$output\n"
        }

//        output = "$output\n$enhancementAlgorithm"

        return output
    }
}