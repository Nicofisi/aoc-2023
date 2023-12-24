import kotlin.math.abs

private fun List<String>.expandDimension(): List<String> {
    return this.flatMap { rowOrColumn ->
        if (rowOrColumn.all { it == '.' }) {
            listOf(rowOrColumn, rowOrColumn) // expansion x 2
        } else {
            listOf(rowOrColumn) // no expansion
        }
    }
}

fun main() {
    val linesBeforeExpansion = readInputLines(day = 11)

    val expandedLines = linesBeforeExpansion.expandDimension()

    val expandedUniverse = expandedLines
        .joinToString(separator = System.lineSeparator())
        .columns()
        .expandDimension()
        .transpose()

    val galaxies = expandedUniverse
        .mapPositionedChars { pos, char -> pos to char }
        .filter { it.second == '#' }
        .map { it.first }

    galaxies.withIndex()
        .flatMap { galaxy1 -> galaxies.withIndex().map { galaxy2 -> galaxy1 to galaxy2 } }
        .filter { it.first.index >= it.second.index }
        .sumOf {
            val pos1 = it.first.value
            val pos2 = it.second.value
            abs(pos1.c - pos2.c) + abs(pos1.r - pos2.r)
        }
        .let { println(it) } // task 1
}
