fun main() {
    val input = readInputString(day = 13)

    val segments = input.split("\n\n", "\r\n\r\n")

    val scores = mutableListOf<Int>()

    segments.withIndex().sumOf { (segmentIndex, segment) ->
        val lines = segment.lines()
        val mirrorHorizontal = lines
            .indices
            .drop(1)
            .find { i ->
                lines.take(i).reversed().zip(lines.drop(i)).all { it.first == it.second }
            }

        var score = mirrorHorizontal?.times(100)

        if (score == null) {
            val columns = segment.columns()
            score = columns
                .indices
                .drop(1)
                .find { i ->
                    columns.take(i).reversed().zip(columns.drop(i)).all { it.first == it.second }
                }!!
        }

        scores.add(score)
        score
    }.let { println(it) } // task 1

    segments.withIndex().sumOf { (segmentIndex, originalSegment) ->
        val previousScore = scores[segmentIndex]

        println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")

        (0 until originalSegment.firstLine().length).forEach { w ->
            (0 until originalSegment.lines().size).forEach { h ->
                val originalSegmentLines = originalSegment.lines()

                val oldPiece = originalSegmentLines[h][w]
                val newPiece = if (oldPiece == '#') '.' else '#'

                val segment = (originalSegmentLines.take(h) + listOf(
                    originalSegmentLines[h].take(w) + newPiece + originalSegmentLines[h].drop(w + 1)
                ) + originalSegmentLines.drop(h + 1))
                    .joinToString(separator = "\n")

                var score: Int? = null

                println(segment)
                println(segmentIndex)
                println()

                val lines = segment.lines()
                val mirrorHorizontal = lines
                    .indices
                    .drop(1)
                    .find { i ->
                        lines.take(i).reversed().zip(lines.drop(i)).all { it.first == it.second }
                    }

                score = mirrorHorizontal?.times(100)

                if (score == null || score == previousScore) {
                    val columns = segment.columns()
                    score = columns
                        .indices
                        .drop(1)
                        .find { i ->
                            columns.take(i).reversed().zip(columns.drop(i)).all { it.first == it.second }
                        }
                }

                if (score != null && score != previousScore) {
                    return@sumOf score!!
                }

                val mirrorHorizontalBack = lines
                    .indices
                    .drop(1)
                    .findLast { i ->
                        lines.take(i).reversed().zip(lines.drop(i)).all { it.first == it.second }
                    }

                score = mirrorHorizontalBack?.times(100)

                if (score == null || score == previousScore) {
                    val columns = segment.columns()
                    score = columns
                        .indices
                        .drop(1)
                        .findLast { i ->
                            columns.take(i).reversed().zip(columns.drop(i)).all { it.first == it.second }
                        }
                }

                if (score != null && score != previousScore) {
                    return@sumOf score!!
                }

            }
        }
        throw RuntimeException("Impossible!")
    }
        .let { println(it) } // task 2
}