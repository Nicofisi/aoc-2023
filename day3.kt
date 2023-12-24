import java.io.File

private typealias PositionPair = Pair<Int, Int>

fun main() {
    val symbolPositions = mutableSetOf<PositionPair>()
    val starPositions = mutableSetOf<PositionPair>()
    val numbersAtPositions = mutableMapOf<PositionPair, Int>()

    File("day3input.txt").bufferedReader().lineSequence().withIndex().forEach { (lineIndex, line) ->
        var currentNumber = ""
        var currentNumberBeginningCharIndex = -1

        fun finishPotentialNumber() {
            if (currentNumber.isEmpty()) {
                return
            }
            numbersAtPositions[lineIndex to currentNumberBeginningCharIndex] = currentNumber.toInt()
            currentNumber = ""
        }

        line.withIndex().forEach { (charIndex, char) ->
            if (char == '.') {
                finishPotentialNumber()
            } else if (char.isDigit()) {
                if (currentNumber.isEmpty()) {
                    currentNumberBeginningCharIndex = charIndex
                }
                currentNumber += char
            } else {
                finishPotentialNumber()
                val position = lineIndex to charIndex
                symbolPositions += position
                if (char == '*') {
                    starPositions += position
                }
            }
        }
        finishPotentialNumber()
    }

    numbersAtPositions
        .filter { (position, number) ->
            val (x, y) = position // x - row, y - column
            val digits = number.toString().length
            (x - 1..x + 1).any { lx ->
                (y - 1..y + digits).any { ly ->
                    symbolPositions.contains(lx to ly)
                }
            }
        }
        .values
        .sum()
        .let { println(it) } // part 1

    val starsWithNumbers = starPositions.associateWith { mutableSetOf<Int>() }

    numbersAtPositions.forEach { (position, number) ->
        val (x, y) = position // x - row, y - column
        val digits = number.toString().length
        (x - 1..x + 1).forEach { lx ->
            (y - 1..y + digits).forEach { ly ->
                if (starPositions.contains(lx to ly)) {
                    starsWithNumbers.getValue(lx to ly) += number
                }
            }
        }
    }

    starsWithNumbers.values
        .filter { it.size == 2 }
        .sumOf { it.iterator().run { next() * next() } }
        .let { println(it) } // part 2
}
