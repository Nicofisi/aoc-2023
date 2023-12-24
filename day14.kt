fun List<String>.printTotalNorthLoad() {
    this.sumOf { column ->
        column.withIndex().sumOf { (i, char) ->
            if (char != 'O') 0 else column.length - i
        }
    }.let { println(it) }
}

fun List<String>.leanTopOrBottom(top: Boolean): List<String> {
    return this.map { column ->
        val split = column.split("#")
        split.joinToString(separator = "#") { segment ->
            val rocks = segment.count { it == 'O' }
            val emptySpaces = segment.length - rocks
            if (top)
                "O".repeat(rocks) + ".".repeat(emptySpaces)
            else
                ".".repeat(emptySpaces) + "O".repeat(rocks)
        }
    }
}

fun main() {
    val input = readInputString(day = 14)

    input.columns().leanTopOrBottom(top = true).printTotalNorthLoad() // task 1

    var state = input.lines()

    val target = 1_000_000_000

    startProgress()
    (0 until target).forEach { index ->
        state = state.transpose().leanTopOrBottom(top = true)
        state = state.transpose().leanTopOrBottom(top = true)
        state = state.transpose().leanTopOrBottom(top = false)
        state = state.transpose().leanTopOrBottom(top = false)

        printProgressEveryNowAndThen(index, target)
    }

    state.forEach(::println)
    state.printTotalNorthLoad() // task 2
}

// .....#....
//....#...O#
//.....##...
//...#......
//.....OOO#.
//.O#...O#.#
//....O#...O
//......OOOO
//#....###.O
//#.OOO#..OO
//
// 69