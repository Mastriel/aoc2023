package dev.calathea.aoc2023.days

private data class SpringRow(val data: String, val parity: List<Int>)

val Day12 = Challenge(FileInput("day12.txt")) {
    val lines = input.split("\r\n")

    val rows = lines.map { line ->
        val (data, parity) = line.split(" ")
        SpringRow(data, parity.split(",").map { it.toInt() })
    }

    var total = 0

    for ((i, row) in rows.withIndex()) {
        val combinations = row.data.count { it == '?' }

        val potentialPatterns = mutableListOf<String>()
        for (combo in 0..Int.MAX_VALUE) {
            val pattern = combo.toString(2)
                .replace("0", ".")
                .replace("1", "#")
                .padStart(combinations, '.')
            if (pattern.length > combinations) break
            potentialPatterns += pattern
        }

        for (pattern in potentialPatterns) {
            if (tryCombination(row, pattern)) {
                total++
            }
        }
    }

    answer(total)
}

private fun FileInput.tryCombination(row: SpringRow, pattern: String) : Boolean {
    var data = row.data

    var patternIndex = 0
    while (data.contains('?')) {
        data = data.replaceFirst('?', pattern[patternIndex])
        patternIndex++
    }

    val parity = row.parity

    val clusterLengths = data.split(".").filter { it.isNotEmpty() }.map { it.length }

    return clusterLengths == parity
}

val Day12Part2 = Challenge(FileInput("day12.txt")) {
    val lines = input.split("\r\n")

    val rows = lines.map { line ->
        val (data, parity) = line.split(" ")
        var betterData = data
        repeat(4) {
            betterData += "?$data"
        }
        val betterParity = parity.toMutableList()
        repeat(4) {
            betterParity += ",$parity".toList()
        }
        SpringRow(betterData, betterParity.toCharArray()
            .concatToString()
            .split(",")
            .filter { it.isNotEmpty() }
            .map { it.toInt() })
    }


    var total = 0L
    val memory = mutableMapOf<Pair<String, List<Int>>, Long>()
    for (row in rows) {
        total += solve(memory, row.data, row.parity)
    }

    answer(total)
}

private typealias MemoryMap = MutableMap<Pair<String, List<Int>>, Long>

private fun FileInput.solve(memory: MemoryMap, chars: String, parity: List<Int>) : Long {
    fun Long.saveToMemory() : Long {
        memory[chars to parity] = this
        return this
    }
    memory[chars to parity]?.let { return it }

    if (chars.isEmpty()) {
        return if (parity.isEmpty()) 1L.saveToMemory() else 0L.saveToMemory()
    }
    val current = chars.first()
    when (current) {
        '.' -> return solve(memory, chars.trim('.'), parity).saveToMemory()
        '?' -> return (solve(memory, chars.replaceFirst("?", "."), parity) +
                solve(memory, chars.replaceFirst("?", "#"), parity)).saveToMemory()
        '#' -> {
            if (parity.isEmpty()) return 0L.saveToMemory()
            if (chars.length < parity.first()) return 0L.saveToMemory()
            for (i in 0..<parity.first()) {
                if (chars[i] == '.') return 0L.saveToMemory()
            }
            if (parity.size > 1) {
                if (chars.length < parity.first() + 1 || chars[parity.first()] == '#')
                    return 0L.saveToMemory()

                return solve(memory, chars.drop(parity.first() + 1), parity.drop(1)).saveToMemory()
            } else {
                return solve(memory, chars.drop(parity.first()), parity.drop(1)).saveToMemory()
            }
        }
    }
    error("bad!")
}