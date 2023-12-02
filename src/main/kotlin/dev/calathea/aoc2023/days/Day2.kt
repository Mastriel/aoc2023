package dev.calathea.aoc2023.days

import kotlin.math.max


private val maxes = mapOf(
    "red" to 12,
    "green" to 13,
    "blue" to 14,
)


val Day2 = Challenge(FileInput("day2.txt")) {
    val lines = input.split("\n")
    var total = 0
    for ((i, line) in lines.withIndex()) {
        val gameNumber = i + 1
        val data = line.split(": ")[1]
        val roundData = data.split("; ")
        var gameValid = true
        for (round in roundData) {
            val blockData = round.split(", ") // ['1 green', '2 blue', ...]
            for (block in blockData) {
                val (amount, color) = block.split(" ")
                if (amount.toInt() > maxes[color.trim()]!!) {
                    gameValid = false
                    break
                }
            }
        }
        if (gameValid) {
            total += gameNumber
        }
    }

    answer(total)
}

val Day2Part2 = Challenge(FileInput("day2.txt")) {
    val lines = input.split("\n")
    var total = 0
    for ((i, line) in lines.withIndex()) {
        val data = line.split(": ")[1]
        val roundData = data.split("; ", ", ")
        val map = mutableMapOf<String, Int>()
        for (round in roundData) {
            val (amount, color) = round.split(" ")
            if (amount.toInt() > (map[color.trim()] ?: 0)) {
                map[color.trim()] = amount.toInt()
            }
        }
        if (map.values.isNotEmpty()) {
            val gameTotal = map.values.reduce { acc, it -> acc * it }
            println("Game ${i}: $gameTotal")
            total += gameTotal
        }
    }

    answer(total)
}