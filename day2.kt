import java.io.File

enum class Color {
    RED, GREEN, BLUE;

    companion object {
        val byLowerName = (values().map { it.name.lowercase() to it }).toMap()
    }
}

data class Game(val id: Int, val draws: List<Map<Color, Int>>)

fun main() {
    // task 1
    readGames().sumOf { game ->
        val isPossible = game.draws.all {
            it.all { (color, count) ->
                when (color) {
                    Color.RED -> count <= 12
                    Color.GREEN -> count <= 13
                    Color.BLUE -> count <= 14
                }
            }
        }

        if (isPossible) game.id else 0
    }.let { println(it) }

    // task 2
    readGames().sumOf { game ->
        Color.values().map { color ->
            game.draws.mapNotNull { draw -> draw[color] }.max()
        }.fold(1) { a, b -> a * b } as Int
    }.let { println(it) }
}

fun readGames(): Sequence<Game> {
    return File("day2input.txt").bufferedReader().lineSequence().map { line ->
        val lineSplit = line.split(":")
        val id = lineSplit[0].removePrefix("Game ").toInt()
        val draws = run {
            val drawsRaw = lineSplit[1].splitToSequence(";")
            drawsRaw.map { drawRaw ->
                drawRaw.splitToSequence(",")
                    .map {
                        val cubesRawSplit = it.trim().split(" ")
                        val amount = cubesRawSplit[0].toInt()
                        val colorName = cubesRawSplit[1]
                        Color.byLowerName.getValue(colorName) to amount
                    }
                    .toMap()
            }
        }
        Game(id, draws.toList())
    }
}