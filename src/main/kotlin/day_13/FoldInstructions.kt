package day_13

data class FoldInstruction(
    val side: String,
    val number: Int,
    var done: Boolean = false
)