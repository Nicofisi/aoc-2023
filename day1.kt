import java.io.File

fun main() {
    val result1 = File("day1input.txt").bufferedReader().lineSequence().sumOf { line ->
        val firstDigit = line.find { it.isDigit() }!!.digitToInt()
        val lastDigit = line.findLast { it.isDigit() }!!.digitToInt()
        (10 * firstDigit) + lastDigit
    }
    println(result1)

    val digitWords = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    val result2 = File("day1input.txt").bufferedReader().lineSequence().sumOf { line ->
        val firstDigitX = (digitWords.asSequence()
            .map { line.indexOf(it) }
            .withIndex()
            .filter { (_, foundIndex) -> foundIndex >= 0 } +
                run {
                    val x = line.withIndex().find { (_, char) -> char.isDigit() }!!
                    listOf(IndexedValue(x.value.digitToInt() - 1, x.index))
                })
            .minBy { it.value }
        val firstDigit = firstDigitX.index + 1

        val lastDigitX = (digitWords.asSequence()
            .map { line.lastIndexOf(it) }
            .withIndex()
            .filter { (_, foundIndex) -> foundIndex >= 0 } +
                run {
                    val x = line.withIndex().findLast { (_, char) -> char.isDigit() }!!
                    listOf(IndexedValue(x.value.digitToInt() - 1, x.index))
                })
            .maxBy { it.value }
        val lastDigit = lastDigitX.index + 1

        (10 * firstDigit) + lastDigit
    }
    println(result2)
}
