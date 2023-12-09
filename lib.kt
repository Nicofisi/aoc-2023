import java.io.File
import java.util.Date

fun String.ints(): List<Int> = Regex("\\d+").findAll(this).map { it.value.toInt() }.toList()

fun String.longs(): List<Long> = Regex("\\d+").findAll(this).map { it.value.toLong() }.toList()

fun String.signedInts(): List<Int> = Regex("-?\\d+").findAll(this).map { it.value.toInt() }.toList()

fun String.signedLongs(): List<Long> = Regex("-?\\d+").findAll(this).map { it.value.toLong() }.toList()


fun String.intsPerLine(): List<List<Int>> = this.lineSequence().map { it.ints() }.toList()

fun String.longsPerLine(): List<List<Long>> = this.lineSequence().map { it.longs() }.toList()

fun String.signedIntsPerLine(): List<List<Int>> = this.lineSequence().map { it.signedInts() }.toList()

fun String.signedLongsPerLine(): List<List<Long>> = this.lineSequence().map { it.signedLongs() }.toList()

fun String.firstLine() = this.lineSequence().first()

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

var lastPrinted = System.currentTimeMillis()

fun printProgressEveryFiveSeconds(index: Int) {
    if (5000 < System.currentTimeMillis() - lastPrinted) {
        lastPrinted = System.currentTimeMillis()
        println("${Date()} | $index")
    }
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
