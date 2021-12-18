package day_14

data class GrowablePolymer(
    var currentPolymer: String,
    val growthInstructions: Map<String, String> // pair to insertion element
)