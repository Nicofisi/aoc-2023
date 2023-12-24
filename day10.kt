import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

enum class Side(val wd: Int, val hd: Int) {
    LEFT(-1, 0), TOP(0, -1), RIGHT(1, 0), BOTTOM(0, 1);

    operator fun component1(): Int = wd
    operator fun component2(): Int = hd
}

val oppositeSides =
    mapOf(Side.LEFT to Side.RIGHT, Side.TOP to Side.BOTTOM, Side.RIGHT to Side.LEFT, Side.BOTTOM to Side.TOP)

data class MazeNodeData(val distance: Int, val connections: List<Side>)

fun main() {
    val lines = readInputString(day = 10)
        .borderWith(".")
        .lines()

//    lines.forEach(::println)

    val startHeightIndex = lines.indexOfFirst { line -> "S" in line }
    val startWidthIndex = lines[startHeightIndex].indexOf("S")

    val stack = ArrayDeque<IndexedValue<LegacyPosition>>()
    stack.add(IndexedValue(0, LegacyPosition(w = startWidthIndex, h = startHeightIndex)))

    val mazeNodes = MutableList(lines.size) { MutableList(lines.first().length) { null as MazeNodeData? } }

    mazeNodes[startHeightIndex][startWidthIndex] = MazeNodeData(0, Side.entries)

    traverseMaze(lines, stack, mazeNodes, canTraverseStart = true)
    while (stack.isNotEmpty()) {
        if (stack.size > 200) {
            throw RuntimeException("stack full your code bad")
        }
        traverseMaze(lines, stack, mazeNodes, canTraverseStart = false)
    }

    mazeNodes.forEach(::println)

    mazeNodes.map { it.filterNotNull() }.maxBy { it.maxOfOrNull { it.distance } ?: 0 }
        .let { line -> line.maxBy { it.distance }.let { println(it.distance) } } // task 1

    val width = lines.size
    val height = lines.first().length

    val cellSize = 5

    val image = BufferedImage(width * cellSize, height * cellSize, BufferedImage.TYPE_INT_ARGB)

    (0 until width * 5).forEach { w ->
        (0 until height * 5).forEach { h ->
            image.setRGB(w, h, Color.WHITE.rgb)
        }
    }

    mazeNodes.withIndex().forEach { (h, nodeLine) ->
        nodeLine.withIndex().forEach { (w, node) ->
            if (node != null) {
                val isStart = w == startWidthIndex && h == startHeightIndex
                val pathColor = if (isStart) Color.RED.rgb else Color(100, 200, 120).rgb

                val borderColor1 = Color.BLACK.rgb

                val borderColor2 = Color.GRAY.rgb

                (0 until cellSize).forEach { wd ->
                    (0 until cellSize).forEach { hd ->
                        image.setRGB(w * cellSize + wd, h * cellSize + hd, borderColor1)
                    }
                }

                (1 until cellSize - 1).forEach { wd ->
                    (1 until cellSize - 1).forEach { hd ->
                        image.setRGB(w * cellSize + wd, h * cellSize + hd, borderColor2)
                    }
                }

                image.setRGB(w * cellSize + 2, h * cellSize + 2, pathColor)

                val char = lines[h][w]

                val sides = when (char) {
                    'S' -> listOf(Side.BOTTOM)
                    'F' -> listOf(Side.BOTTOM, Side.RIGHT)
                    '7' -> listOf(Side.LEFT, Side.BOTTOM)
                    'J' -> listOf(Side.LEFT, Side.TOP)
                    'L' -> listOf(Side.RIGHT, Side.TOP)
                    '|' -> listOf(Side.TOP, Side.BOTTOM)
                    '-' -> listOf(Side.LEFT, Side.RIGHT)
                    '.' -> emptyList()
                    else -> throw IllegalArgumentException()

                }

                if (sides.contains(Side.BOTTOM)) {
                    image.setRGB(w * cellSize + 2, h * cellSize + 3, pathColor)
                    image.setRGB(w * cellSize + 2, h * cellSize + 4, pathColor)
                    image.setRGB(w * cellSize + 1, h * cellSize + 3, borderColor2)
                    image.setRGB(w * cellSize + 3, h * cellSize + 3, borderColor2)
                    image.setRGB(w * cellSize + 1, h * cellSize + 4, borderColor2)
                    image.setRGB(w * cellSize + 3, h * cellSize + 4, borderColor2)
                }

                if (sides.contains(Side.TOP)) {
                    image.setRGB(w * cellSize + 2, h * cellSize + 1, pathColor)
                    image.setRGB(w * cellSize + 2, h * cellSize + 0, pathColor)
                    image.setRGB(w * cellSize + 1, h * cellSize + 1, borderColor2)
                    image.setRGB(w * cellSize + 3, h * cellSize + 1, borderColor2)
                    image.setRGB(w * cellSize + 1, h * cellSize + 0, borderColor2)
                    image.setRGB(w * cellSize + 3, h * cellSize + 0, borderColor2)
                }

                if (sides.contains(Side.RIGHT)) {
                    image.setRGB(w * cellSize + 3, h * cellSize + 2, pathColor)
                    image.setRGB(w * cellSize + 4, h * cellSize + 2, pathColor)
                    image.setRGB(w * cellSize + 3, h * cellSize + 1, borderColor2)
                    image.setRGB(w * cellSize + 3, h * cellSize + 3, borderColor2)
                    image.setRGB(w * cellSize + 4, h * cellSize + 1, borderColor2)
                    image.setRGB(w * cellSize + 4, h * cellSize + 3, borderColor2)
                }

                if (sides.contains(Side.LEFT)) {
                    image.setRGB(w * cellSize + 1, h * cellSize + 2, pathColor)
                    image.setRGB(w * cellSize + 0, h * cellSize + 2, pathColor)
                    image.setRGB(w * cellSize + 1, h * cellSize + 1, borderColor2)
                    image.setRGB(w * cellSize + 1, h * cellSize + 3, borderColor2)
                    image.setRGB(w * cellSize + 0, h * cellSize + 1, borderColor2)
                    image.setRGB(w * cellSize + 0, h * cellSize + 3, borderColor2)
                }

            }
        }
    }

    ImageIO.write(image, "png", File("day10output.png"))

    // <here  manually color the inside tiles with RGB = 34, 177, 76 and save the file as day10outputpaint.png>

    val colorFromPaint = Color(34, 177, 76).rgb

    val paintFile = File("day10outputpaint.png")

    if (paintFile.exists()) {
        val readImage = ImageIO.read(paintFile)
        val colorSum = (0 until readImage.width).sumOf { w ->
            (0 until readImage.height).count { h ->
                readImage.getRGB(w, h) == colorFromPaint
            }
        }
        (colorSum / (cellSize * cellSize)).let { println(it) } // task 2
    }
}

private fun traverseMaze(
    lines: List<String>,
    stack: ArrayDeque<IndexedValue<LegacyPosition>>,
    mazeNodes: MutableList<MutableList<MazeNodeData?>>,
    canTraverseStart: Boolean,
) {
//    stack.forEach(::println)

    val (distance, pos) = stack.removeFirst()

    val symbol = lines[pos.h][pos.w]

//    if (Random.nextInt(1000) == 0) {
//        mazeNodes.forEach(::println)
//        println("Traversing $symbol $pos")
//        println()
//    }

    if (mazeNodes[pos.h][pos.w] != null && (!canTraverseStart || symbol != 'S')) {
//        println("a")
        return // already traversed
    }

    val sides = when (symbol) {
        'S' -> listOf(Side.BOTTOM, Side.TOP, Side.LEFT, Side.RIGHT)
        'F' -> listOf(Side.BOTTOM, Side.RIGHT)
        '7' -> listOf(Side.LEFT, Side.BOTTOM)
        'J' -> listOf(Side.LEFT, Side.TOP)
        'L' -> listOf(Side.RIGHT, Side.TOP)
        '|' -> listOf(Side.TOP, Side.BOTTOM)
        '-' -> listOf(Side.LEFT, Side.RIGHT)
        '.' -> emptyList<Side>()
        else -> throw IllegalArgumentException()
    }

    val bestConnectingMazeSide = Side.entries.filter { side ->
        side in sides
    }.filter { side ->
        val (wd, hd) = side
        val sidePos = pos.copy(w = pos.w + wd, h = pos.h + hd)
        val neighborNode = mazeNodes[sidePos.h][sidePos.w]
        neighborNode != null && neighborNode.connections.contains(oppositeSides[side])
    }.minByOrNull { (wd, hd) ->
        val sidePos = pos.copy(w = pos.w + wd, h = pos.h + hd)
        mazeNodes[sidePos.h][sidePos.w]!!.distance
    }

    if (bestConnectingMazeSide == null && symbol != 'S') {
//        println("b")
        return
    }

    mazeNodes[pos.h][pos.w] = MazeNodeData(distance, sides)

//    val (bestSideWd, bestSideHd) = bestConnectingMazeSide
//    val sidePos = pos.copy(w = pos.w + wd, h = pos.h + hd)
//    val distance = mazeNodes[sidePos.h][sidePos.w]?.distance?.plus(1) ?: 0

    Side.entries.forEach { side ->
        val (wd, hd) = side
        val sidePos = pos.copy(w = pos.w + wd, h = pos.h + hd)
        stack.add(IndexedValue(distance + 1, sidePos))
    }

//    println(bestConnectingMazeSide)
}