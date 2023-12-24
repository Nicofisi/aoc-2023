fun main() {
    val lines = readInputLines(8)
    val booleanInstructions = lines.first().map { it == 'L' }

    val instructions: Sequence<Boolean> = sequence { while (true) yieldAll(booleanInstructions) }

    val movements =
        lines.drop(2)
            .map { line -> Regex("([A-Z]{3}) = \\(([A-Z]{3}), ([A-Z]{3})\\)").find(line)!!.groupValues.slice(1..3) }
            .associate { nums -> nums[0] to (nums[1] to nums[2]) }

    solveTask1(0, "AAA", instructions, movements)

    val startingMovements = movements.keys.filter { it.endsWith("A") }

    startingMovements.map { startingMovement ->
        solvePartOfTask2(0, startingMovement, instructions, movements).toLong()
    }.lcm().let { println(it) } // task 2 - 20 220 305 520 997
}

private tailrec fun solvePartOfTask2(i: Int, position: String, instructions: Sequence<Boolean>, movements: Map<String, Pair<String, String>>): Int {
    printProgressEveryNowAndThen(i)
    if (position.endsWith("Z")) {
        return i
    }
    val instruction = instructions.first()
    val movementOptions = movements.getValue(position)
    val nextPosition = if (instruction) movementOptions.first else movementOptions.second
    return solvePartOfTask2(i + 1, nextPosition, instructions.drop(1), movements)
}

private tailrec fun solveTask1(i: Int, position: String, instructions: Sequence<Boolean>, movements: Map<String, Pair<String, String>>) {
    if (position == "ZZZ") {
        println(i) // task 1
        return
    }
    val instruction = instructions.first()
    val movementOptions = movements.getValue(position)
    val nextPosition = if (instruction) movementOptions.first else movementOptions.second
    solveTask1(i + 1, nextPosition, instructions.drop(1), movements)
}


//private tailrec fun solveTask2(i: Int, positions: Array<String>, instructions: List<Boolean>, allInstructions: List<Boolean>, movements: Map<String, Pair<String, String>>) {
//    printProgressEveryFiveSeconds(i)
//    if (instructions.isEmpty()) {
//        return solveTask2(i, positions, allInstructions, allInstructions, movements)
//    }
//    if (positions.all { it[2] == 'Z' }) {
//        println(i) // task 2
//        return
//    }
//    val instruction = instructions.first()
//    positions.indices.forEach { i ->
//        val movementOptions = movements.getValue(positions[i])
//        positions[i] = if (instruction) movementOptions.first else movementOptions.second
//    }
//    solveTask2(i + 1, positions, instructions.drop(1), allInstructions, movements)
//}

//private tailrec fun solveTask2(i: Int, positions: List<String>, instructions: Sequence<Boolean>, movements: Map<String, Pair<String, String>>) {
//    printProgressEveryFiveSeconds(i)
//    if (positions.all { it.endsWith("Z") }) {
//        println(i) // task 2
//        return
//    }
//    val instruction = instructions.first()
//    val nextPositions = positions.map { position ->
//        val movementOptions = movements.getValue(position)
//        if (instruction) movementOptions.first else movementOptions.second
//    }
//    solveTask2(i + 1, nextPositions, instructions.drop(1), movements)
//}
