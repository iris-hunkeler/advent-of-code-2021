package day_15

data class RiskLevelMap(
    private val map: List<List<Int>>
) {
    companion object {
        fun encodeNode(x: Int, y: Int) = "$x-$y"

        fun decodeNode(node: String) = Pair(node.split("-")[0].toInt(), node.split("-")[1].toInt())
    }

    fun getSizeX() = map[0].size
    fun getSizeY() = map.size

    fun getStartNode() = encodeNode(0, 0)

    fun getEndNode() = encodeNode(map[0].size - 1, map.size - 1)

    fun getRisk(node: String): Int {
        val (x, y) = decodeNode(node)
        return map[y][x]
    }

    fun getAllNeighbors(node: String): List<String> {
        val (x, y) = decodeNode(node)

        val neighbors = mutableListOf<String>()
        getUpperNeighbor(x, y)?.let { neighbors.add(it) }
        getRightNeighbor(x, y)?.let { neighbors.add(it) }
        getLowerNeighbor(x, y)?.let { neighbors.add(it) }
        getLeftNeighbor(x, y)?.let { neighbors.add(it) }

        return neighbors
    }

    private fun getUpperNeighbor(x: Int, y: Int) = if (x > 0) {
        encodeNode(x - 1, y)
    } else {
        null
    }

    private fun getRightNeighbor(x: Int, y: Int) = if (y < map[0].size - 1) {
        encodeNode(x, y + 1)
    } else {
        null
    }

    private fun getLowerNeighbor(x: Int, y: Int) = if (x < map.size - 1) {
        encodeNode(x + 1, y)
    } else {
        null
    }

    private fun getLeftNeighbor(x: Int, y: Int) = if (y > 0) {
        encodeNode(x, y - 1)
    } else {
        null
    }

    override fun toString(): String {
        var output = ""

        map.forEach { line ->
            line.forEach { value ->
                output = "$output$value"
            }
            output = "$output\n"
        }
        return output
    }
}