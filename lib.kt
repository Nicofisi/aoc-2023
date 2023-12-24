import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.util.Date

data class LegacyPosition(val w: Int, val h: Int)

data class Position(val r: Int, val c: Int)

fun String.ints(): List<Int> = Regex("\\d+").findAll(this).map { it.value.toInt() }.toList()

fun String.longs(): List<Long> = Regex("\\d+").findAll(this).map { it.value.toLong() }.toList()

fun String.signedInts(): List<Int> = Regex("-?\\d+").findAll(this).map { it.value.toInt() }.toList()

fun String.signedLongs(): List<Long> = Regex("-?\\d+").findAll(this).map { it.value.toLong() }.toList()

fun String.intsPerLine(): List<List<Int>> = this.lineSequence().map { it.ints() }.toList()

fun String.longsPerLine(): List<List<Long>> = this.lineSequence().map { it.longs() }.toList()

fun String.signedIntsPerLine(): List<List<Int>> = this.lineSequence().map { it.signedInts() }.toList()

fun String.signedLongsPerLine(): List<List<Long>> = this.lineSequence().map { it.signedLongs() }.toList()

fun String.firstLine() = this.lineSequence().first()

fun String.borderWith(border: String): String {
    check(this.isNotEmpty())
    checkAllLinesSameLength()

    val firstLineLength = this.firstLine().length

    return border.repeat(firstLineLength + 2) +
            System.lineSeparator() +
            this.lineSequence().joinToString(separator = System.lineSeparator()) { border + it + border } +
            System.lineSeparator() +
            border.repeat(firstLineLength + 2)
}

fun String.columns(): List<String> {
    check(this.isNotEmpty())
    checkAllLinesSameLength()

    return this.firstLine().indices.map { i ->
        this.lines().joinToString(separator = "") { line -> line[i].toString() }
    }
}

fun List<String>.transpose(): List<String> {
    check(this.isNotEmpty())

    val firstColumnLength = this.first().length
    check(this.all { it.length == firstColumnLength })

    return this.first().indices.map { i ->
        this.joinToString(separator = "") { line -> line[i].toString() }
    }
}

private fun String.checkAllLinesSameLength() {
    val firstLineLength = this.firstLine().length
    check(this.lines().all { it.length == firstLineLength })
}

fun <T> List<String>.mapIndexedChars(action: (r: Int, c: Int, char: Char) -> T): List<T> {
    return this.withIndex().flatMap { (r, row) ->
        row.withIndex().map { (c, char) ->
            action(r, c, char)
        }
    }
}

fun <T> List<String>.mapPositionedChars(action: (pos: Position, char: Char) -> T): List<T> {
    return mapIndexedChars { r, c, char -> action(Position(r = r, c = c), char) }
}

fun <T> List<List<T>>.filterNotEmpty(): List<List<T>> = this.filterNot { it.isEmpty() }

fun readInputLines(day: Int) = File("day${day}input.txt").readLines()

fun readInputLinesSequence(day: Int) = File("day${day}input.txt").bufferedReader().lineSequence()

fun readInputString(day: Int) = File("day${day}input.txt").readText()

fun List<Long>.product(): Long {
    check(isNotEmpty())

    return fold(1L) { acc: Long, i: Long -> acc * i }
}

fun List<Int>.product(): Int {
    check(isNotEmpty())

    return fold(1) { acc: Int, i: Int -> acc * i }
}

var firstPrinted: LocalDateTime? = null
var lastPrinted = System.currentTimeMillis()
var lastProgressIndex = 0

fun startProgress() {
    firstPrinted = LocalDateTime.now()
    lastPrinted = System.currentTimeMillis()
    lastProgressIndex = 0
}

fun printProgressEveryNowAndThen(index: Int, target: Int? = null, everyMillis: Int = 5000) {
    if (everyMillis < System.currentTimeMillis() - lastPrinted) {
        lastPrinted = System.currentTimeMillis()
        val progressDelta = index - lastProgressIndex
        val currentRate = progressDelta * 1000 / everyMillis
        val indicesLeft = target?.minus(index)
        val secondsLeft = indicesLeft?.div(progressDelta)?.toDouble()
        val minutesLeft = secondsLeft?.div(60)
        val hoursLeft = minutesLeft?.div(60)
        println("${Date()} | $index steps done | rate $currentRate per second | ${"%.2f".format(hoursLeft)} hours left")
        lastProgressIndex = index
    }
}

private fun calcMinutesLeftStr(target: Int?, index: Int): String {
    if (target == null) return ""
    val tookSoFar = Duration.between(firstPrinted, LocalDateTime.now())
    val tookSoFarMillis = tookSoFar.toMillis()
    return " | " + tookSoFarMillis * (((target / index)) / (1000 * 60)) + " minutes left"
}

// Function to find the GCD of two numbers using Euclid's algorithm
fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}

// Function to find the LCM of two numbers using the formula
fun lcm(a: Long, b: Long): Long {
    return a * b / gcd(a, b)
}

// Function to find the LCM of a list of numbers using a loop
fun List<Long>.lcm(): Long {
    var result = this[0]
    for (i in 1 until this.size) {
        result = lcm(result, this[i])
    }
    return result
}
