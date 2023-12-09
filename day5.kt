import java.io.File
import java.lang.RuntimeException

data class Instruction(val destRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long)

fun main() {
    val entireFileString = File("day5input.txt").readText()
    val fileSections = entireFileString.split("\n\n", "\r\n\r\n")

    val seeds = fileSections.first().longs()

    val converters = fileSections.drop(1).map { section ->
        val (from, to) = section.firstLine().split(" ").first().split("-to-")
        (from to to) to section.longsPerLine().filterNotEmpty().map { lineLongs ->
            Instruction(destRangeStart = lineLongs[0], sourceRangeStart = lineLongs[1], rangeLength = lineLongs[2])
        }
    }

    fun Iterator<Long>.printMinLocation() = this.asSequence().map { seed ->
        var currentCategory = "seed"
        var currentValue = seed

        while (true) {
            val (converterType, instructions) = converters.find { it.first.first == currentCategory }
                ?: return@map currentValue
            instructions.find { inst ->
                currentValue in inst.sourceRangeStart until inst.sourceRangeStart + inst.rangeLength
            }?.let { inst ->
                currentValue = inst.destRangeStart + (currentValue - inst.sourceRangeStart)
            }

            currentCategory = converterType.second
        }

        throw RuntimeException("unreachable")
    }
        .min()
        .let { println(it) }

    seeds.iterator().printMinLocation()

    val seedRanges = seeds.chunked(2).map { it[0] until it[0] + it[1] }

    seedRanges.asSequence().flatten().iterator().printMinLocation()
}