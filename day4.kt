import java.io.File

data class DayCard(
    val count: Int,
    val winningNumbers: List<Int>,
    val myNumbers: List<Int>,
    val myWinningNumbers: List<Int>
)

fun main() {
    val lines = File("day4input.txt").readLines()

    val cards = lines.map { line ->
        val (cardId, numbers) = line.split(":")
        val (winningNumbers, myNumbers) = numbers.split("|")
            .map { it.trim() }
            .map { numsStr -> numsStr.split(Regex("\\s+")).map { it.toInt() } }

        val myWinningNumbers = myNumbers.filter { winningNumbers.contains(it) }

        DayCard(count = 1, winningNumbers = winningNumbers, myNumbers = myNumbers, myWinningNumbers = myWinningNumbers)
    }.toMutableList()

    cards
        .sumOf { card -> card.myWinningNumbers.fold(0) { acc, _ -> if (acc == 0) 1 else (acc * 2) } as Int }
        .let { println(it) } // task 1

    cards.forEachIndexed { i, dayCard ->
        if (dayCard.myWinningNumbers.isNotEmpty()) {
            (i + 1..i + dayCard.myWinningNumbers.size).forEach { x ->
                cards[x] = cards[x].copy(count = cards[x].count + dayCard.count)
            }
        }
    }

    cards.sumOf { it.count }.let { println(it) } // task 2
}