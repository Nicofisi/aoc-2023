fun main() {
    val lines = readInputString(day = 9).signedLongsPerLine()

    lines
        .sumOf { line ->
            calcNextValue(previousLastValues = emptyList(), line = line)
        }
        .let(::println) // task 1

    lines
        .sumOf { line ->
            calcPrevValue(previousFirstValues = emptyList(), line = line)
        }
        .let(::println) // task 2
}

private tailrec fun calcNextValue(previousLastValues: List<Long>, line: List<Long>): Long {
    if (line.all { it == 0L }) {
        return calcNextValueResult(previousLastValues, 0L)
    }
    val nextLine = line.zipWithNext { a, b -> b - a }
    val nextPreviousLastValues = previousLastValues + line.last()
    return calcNextValue(nextPreviousLastValues, nextLine)
}

private tailrec fun calcNextValueResult(previousLastValues: List<Long>, lastValue: Long): Long {
    if (previousLastValues.isEmpty()) {
        return lastValue
    }
    return calcNextValueResult(previousLastValues.dropLast(1), previousLastValues.last() + lastValue)
}

private tailrec fun calcPrevValue(previousFirstValues: List<Long>, line: List<Long>): Long {
    if (line.all { it == 0L }) {
        return calcPrevValueResult(previousFirstValues, 0L)
    }
    val nextLine = line.zipWithNext { a, b -> b - a }
    val nextPreviousFirstValues = previousFirstValues + line.first()
    return calcPrevValue(nextPreviousFirstValues, nextLine)
}

private tailrec fun calcPrevValueResult(previousFirstValues: List<Long>, firstValue: Long): Long {
    if (previousFirstValues.isEmpty()) {
        return firstValue
    }
    return calcPrevValueResult(previousFirstValues.dropLast(1), previousFirstValues.last() - firstValue)
}