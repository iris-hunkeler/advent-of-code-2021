package day_17

data class TargetArea(
    val startX: Int,
    val endX: Int,
    val startY: Int,
    val endY: Int
) {
    fun hits(x: Int, y: Int): Pair<Boolean, Int> {
        var maxY = Int.MIN_VALUE

        var velocityX = x
        var velocityY = y
        var positionX = 0
        var positionY = 0
        var step = 0

        while (positionY >= startY) {
//            println("STEP $step")
//            println("velocity: x = $velocityX, y = $velocityY")
//            println("position: x = $positionX, y = $positionY")
            step++

            // adjust position
            positionX += velocityX
            positionY += velocityY

            // adjust velocity
            if (velocityX > 0) {
                velocityX--
            } else if (velocityX < 0)  {
                velocityY++
            }
            velocityY--

            // check y
            if (positionY > maxY) {
                maxY = positionY
            }

            if (positionIsWithinTargetArea(positionX, positionY)) {
                return Pair(true, maxY)
            }
        }

        // probe is below target area and has not hit it
        return Pair(false, maxY)
    }

    private fun positionIsWithinTargetArea(x: Int, y: Int): Boolean {
        if (x in (startX)..endX && y in (startY)..endY) {
            return true
        }
        return false
    }
}