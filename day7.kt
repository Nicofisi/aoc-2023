fun main() {
    val cardValueOrder = "A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2".split(", ").map { it.first() }

    fun String.kindScoreTask1(): Int {
        val occurrenceCountsDesc = this
            .groupBy { it }
            .mapValues { it.value.size }
            .values
            .sortedDescending()

        val mostCommonCount = occurrenceCountsDesc[0]
        val secondMostCommonCount = occurrenceCountsDesc.getOrNull(1) ?: 99999

        return mostCommonCount * 10 + secondMostCommonCount // 10 is an arbitrary number, prob could be 2 or 3
    }

    fun String.numberValueTask1() = this
        .replace("T", "a") // XD
        .replace("J", "b")
        .replace("Q", "c")
        .replace("K", "d")
        .replace("A", "e")
        .toInt(15)

    val handsWithBids = readInputLines(7).map { line ->
        line.split(" ").let { it[0] to it[1].toInt() }
    }

//    handsWithBids
//        .sortedWith(compareBy({ it.first.kindScore() }, { it.first.numberValue() }))
//        .forEach { println("$it - ${it.first.kindScore()} - ${it.first.numberValue()} ") }

    handsWithBids
        .sortedWith(compareBy({ it.first.kindScoreTask1() }, { it.first.numberValueTask1() }))
        .withIndex()
//        .onEach { (index, handWithBid) ->
//            val (hand, bid) = handWithBid
//            println("$index. $handWithBid - ${hand.kindScore()} - ${hand.numberValue()}")
//        }
        .map { (index, handWithBid) ->
            val (hand, bid) = handWithBid
            return@map (index + 1) * bid.toLong()
        }
        .sum()
        .let(::println) // task 1

    fun String.kindScoreTask2(): Int {

        val occurrenceCountsDesc = this
            .groupBy { it }
            .filterKeys { it != 'J' }
            .mapValues { it.value.size }
            .values
            .sortedDescending()

        val jCount = this.count { it == 'J' }

        val mostCommonCount = occurrenceCountsDesc.getOrElse(0) { 0 } + jCount
        val secondMostCommonCount = occurrenceCountsDesc.getOrElse(1) { 99999 }

        return mostCommonCount * 10 + secondMostCommonCount // 10 is an arbitrary number, prob could be 2 or 3
    }

    fun String.numberValueTask2() = this
        .replace("T", "a") // XD
        .replace("J", "1")
        .replace("Q", "b")
        .replace("K", "c")
        .replace("A", "d")
        .toInt(14)

    handsWithBids
        .sortedWith(compareBy({ it.first.kindScoreTask2() }, { it.first.numberValueTask2() }))
        .withIndex()
        .onEach { (index, handWithBid) ->
            val (hand, bid) = handWithBid
            println("$index. $handWithBid - ${hand.kindScoreTask2()} - ${hand.numberValueTask2()}")
        }
        .map { (index, handWithBid) ->
            val (hand, bid) = handWithBid
            return@map (index + 1) * bid.toLong()
        }
        .sum()
        .let(::println) // task 1
}