fun main() {
    run {
        val (times, recordDistances) = readInputLines(day = 6)
            .map { it.longs() }

        solve(times, recordDistances) // task 1
    }

    run {
        val (times, recordDistances) = readInputLines(day = 6)
            .map { it.replace(Regex("\\s+"), "").longs() }

        solve(times, recordDistances) // task 2
    }
}

private fun solve(
    times: List<Long>,
    recordDistances: List<Long>
) {
    times
        .zip(recordDistances)
        .map { (time, recordDistance) ->
            (1 until time)
                .map { wait -> (time - wait) * wait }
                .count { distance -> distance > recordDistance }
        }
        .product()
        .let(::println)
}
