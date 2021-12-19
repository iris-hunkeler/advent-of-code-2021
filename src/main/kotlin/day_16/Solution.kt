package day_16

import InputType
import java.io.File

private const val VERSION_START = 0
private const val VERSION_END = 3
private const val TYPE_START = VERSION_END
private const val TYPE_END = 6
private const val CONTENT_START = TYPE_END

private const val OPERATOR_SIZE = 1

enum class PacketType(val id: Int) {
    SUM(0),
    PRODUCT(1),
    MINIMUM(2),
    MAXIMUM(3),
    VALUE(4),
    GREATER_THAN(5),
    LESS_THAN(6),
    EQUALS_TO(7);

    companion object {
        fun findById(id: Int): PacketType = values().find { it.id == id } ?: error("PacketType with id $id not found")
    }
}

enum class LengthType(val id: Int, val bitsForLength: Int) {
    TOTAL_LENGTH(0, 15),
    NUMBER_OF_SUB_PACKETS(1, 11);

    companion object {
        fun findById(id: Int): LengthType = values().find { it.id == id } ?: error("LengthType with id $id not found")
    }
}


fun main(args: Array<String>) {
    val input = getInput(InputType.INPUT)

    input.forEachIndexed { index, input ->
        println("original: $input")
        val binary = convertHexadecimalToBinary(input)

        val (value, summarizeVersionNumbers, _) = handlePackage(binary)
        println("*** Result 1 ($index): $summarizeVersionNumbers ***")
        println("*** Result 2 ($index): $value ***")
    }
}

fun handlePackage(binary: String): Triple<Long, Int, Int> {
    println("binary: $binary")

    var totalVersionNumbers = 0
    var bitsRead = 0
    val value: Long

    val (version, type) = extractVersionAndType(binary)
    println("version: $version | type: $type")
    totalVersionNumbers += version
    bitsRead += 6

    if (type == PacketType.VALUE) {
        // literal value

        val (valueHere, bitsReadHere) = readLiteralValue(binary.substring(CONTENT_START))
        bitsRead += bitsReadHere
        value = valueHere
        println("literal = $valueHere")
    } else {
        // operator

        val (valueHere, versionNumbersHere, bitsReadHere) = readOperatorValue(type, binary.substring(CONTENT_START))
        value = valueHere
        totalVersionNumbers += versionNumbersHere
        bitsRead += bitsReadHere
    }

    return Triple(value, totalVersionNumbers, bitsRead)
}

fun readOperatorValue(type: PacketType, binary: String): Triple<Long, Int, Int> {
    var totalVersionNumbers = 0
    var bitsRead = 0

    val lengthTypeId = Integer.parseInt(binary.substring(0, 0 + OPERATOR_SIZE), 2)
    val lengthType = LengthType.findById(lengthTypeId)
    bitsRead += 1

    val lengthEnd = OPERATOR_SIZE + lengthType.bitsForLength
    val length = Integer.parseInt(binary.substring(1, lengthEnd), 2)
    bitsRead += lengthType.bitsForLength

    var subPacket = binary.substring(lengthEnd)
    val values = mutableListOf<Long>()

    if (lengthType == LengthType.TOTAL_LENGTH) {
        var bitsReadInOperatorPacket = 0

        while (bitsReadInOperatorPacket < length) {
            val (valueHere, versionNumbersSub, bitsReadHere) = handlePackage(subPacket)
            values.add(valueHere)
            bitsReadInOperatorPacket += bitsReadHere
            totalVersionNumbers += versionNumbersSub

            subPacket = subPacket.substring(bitsReadHere)
        }
        bitsRead += bitsReadInOperatorPacket
    } else if (lengthType == LengthType.NUMBER_OF_SUB_PACKETS) {
        var numberOfSubPacketsRead = 0
        var bitsReadInOperatorPacket = 0

        while (numberOfSubPacketsRead < length) {
            numberOfSubPacketsRead++
            val (valueHere, versionNumbersSub, bitsReadHere) = handlePackage(subPacket)
            values.add(valueHere)
            bitsReadInOperatorPacket += bitsReadHere
            totalVersionNumbers += versionNumbersSub

            subPacket = subPacket.substring(bitsReadHere)
        }
        bitsRead += bitsReadInOperatorPacket
    }

    val value = executeValueCalculation(type, values)

    return Triple(value, totalVersionNumbers, bitsRead)
}

private fun executeValueCalculation(type: PacketType, values: MutableList<Long>): Long {
    var value = 0L

    if (type == PacketType.SUM) {
        value = values.sum()
    } else if (type == PacketType.PRODUCT) {
        value = values.reduce { acc, i -> acc * i }
    } else if (type == PacketType.MINIMUM) {
        value = values.minOrNull()!!
    } else if (type == PacketType.MAXIMUM) {
        value = values.maxOrNull()!!
    } else if (type == PacketType.GREATER_THAN) {
        if (values[0] > values[1]) {
            value = 1
        } else {
            value = 0
        }
    } else if (type == PacketType.LESS_THAN) {
        if (values[0] < values[1]) {
            value = 1
        } else {
            value = 0
        }
    } else if (type == PacketType.EQUALS_TO) {
        if (values[0] == values[1]) {
            value = 1
        } else {
            value = 0
        }
    }
    return value
}

private fun readLiteralValue(content: String): Pair<Long, Int> {
    val builder = StringBuilder()

    content
        .chunked(5) // packets are 5 bits each
        .forEachIndexed { index, packet ->
            builder.append(packet.substring(1))

            if (packet[0] == '0') {
                val value = builder.toString().toLong(2)
                val bitsRead = (index + 1) * 5

                return Pair(value, bitsRead)
            }
        }

    error("failed parsing")
}

private fun extractVersionAndType(element: String): Pair<Int, PacketType> {
    val version = Integer.parseInt(element.substring(VERSION_START, VERSION_END), 2)
    val type = Integer.parseInt(element.substring(TYPE_START, TYPE_END), 2)
    return Pair(version, PacketType.findById(type))
}

fun convertHexadecimalToBinary(input: String): String {
    val builder = StringBuilder()

    input.forEach {
        val inputAsInt = Integer.parseInt(it.toString(), 16)
        val inputAsString = Integer.toBinaryString(inputAsInt)
        val inputPadded = inputAsString.padStart(4, '0')
        builder.append(inputPadded)
    }

    return builder.toString()
}


private fun getInput(inputType: InputType): List<String> {
    if (inputType == InputType.SAMPLE) {
        // samples for part 1
//        return listOf(
//            "38006F45291200",
//            "EE00D40C823060",
//            "8A004A801A8002F478",  //16
//            "620080001611562C8802118E34", // 12
//            "C0015000016115A2E0802F182340", //23
//            "A0016C880162017C3686B18A3D4780" //31
//        )

        // samples for part 2
        return listOf(
            "C200B40A82", // 3
            "04005AC33890", //54
            "880086C3E88112", // 7
            "CE00C43D881120", // 9
            "D8005AC2A8F0", // 1
            "F600BC2D8F", // 0
            "9C005AC2F8F0", // 0
            "9C0141080250320F1802104A08", // 1
        )
    } else {

        return File("src/main/kotlin/day_16/${inputType.fileName}.txt")
            .readLines()
    }
}
