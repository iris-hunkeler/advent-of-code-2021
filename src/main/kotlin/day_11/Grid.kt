package day_11

data class Grid(
    var map: MutableList<MutableList<Int>>
) {
    fun getPoint(x: Int, y: Int) = Point(x, y, map[x][y])

    fun getAllAdjacentPoints(point: Point): List<Point> {
        val adjacentPoints = mutableListOf<Point>()
        getUpperPoint(point)?.let { adjacentPoints.add(it) }
        getUpperRightPoint(point)?.let { adjacentPoints.add(it) }
        getRightPoint(point)?.let { adjacentPoints.add(it) }
        getLowerRightPoint(point)?.let { adjacentPoints.add(it) }
        getLowerPoint(point)?.let { adjacentPoints.add(it) }
        getLowerLeftPoint(point)?.let { adjacentPoints.add(it) }
        getLeftPoint(point)?.let { adjacentPoints.add(it) }
        getUpperLeftPoint(point)?.let { adjacentPoints.add(it) }

        return adjacentPoints
    }

    fun getUpperPoint(point: Point) = if (point.x > 0) {
        Point(point.x - 1, point.y, map[point.x - 1][point.y])
    } else {
        null
    }

    fun getUpperRightPoint(point: Point) = if (point.x > 0 && point.y < map[0].size - 1) {
        Point(point.x - 1, point.y + 1, map[point.x - 1][point.y + 1])
    } else {
        null
    }

    fun getRightPoint(point: Point) = if (point.y < map[0].size - 1) {
        Point(point.x, point.y + 1, map[point.x][point.y + 1])
    } else {
        null
    }

    fun getLowerRightPoint(point: Point) = if (point.x < map.size - 1 && point.y < map[0].size - 1) {
        Point(point.x + 1, point.y + 1, map[point.x + 1][point.y + 1])
    } else {
        null
    }

    fun getLowerPoint(point: Point) = if (point.x < map.size - 1) {
        Point(point.x + 1, point.y, map[point.x + 1][point.y])
    } else {
        null
    }

    fun getLowerLeftPoint(point: Point) = if (point.x < map.size - 1 && point.y > 0) {
        Point(point.x + 1, point.y - 1, map[point.x + 1][point.y - 1])
    } else {
        null
    }

    fun getLeftPoint(point: Point) = if (point.y > 0) {
        Point(point.x, point.y - 1, map[point.x][point.y - 1])
    } else {
        null
    }

    fun getUpperLeftPoint(point: Point) = if (point.x > 0 && point.y > 0) {
        Point(point.x - 1, point.y - 1, map[point.x - 1][point.y - 1])
    } else {
        null
    }

    override fun toString(): String {
        var output = ""

        map.forEachIndexed { x, line ->
            line.forEachIndexed { y, value ->
                output = "$output$value"
            }
            output = "$output\n"
        }
        return output
    }
}