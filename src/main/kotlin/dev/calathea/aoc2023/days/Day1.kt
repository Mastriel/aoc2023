package dev.calathea.aoc2023.days

private val numbers = "0123456789".toCharArray()
private val wordsToNumbers = mapOf(
    "one" to '1',
    "two" to '2',
    "three" to '3',
    "four" to '4',
    "five" to '5',
    "six" to '6',
    "seven" to '7',
    "eight" to '8',
    "nine" to '9',
)

val Day1 = Challenge(FileInput("day1.txt")) {

    val lines = input.split("\n")

    var total = 0
    for (line in lines) {
        var firstDigit: Char? = null
        var lastDigit: Char? = null
        for (char in line) {
            if (char in numbers) {
                if (firstDigit == null) firstDigit = char
                lastDigit = char
            }
        }

        total += "$firstDigit$lastDigit".toInt()
    }

    answer(total)
}

val Day1Part2 = Challenge(FileInput("day1.txt")) {
    val lines = input.split("\n")

    var total = 0

    for (line in lines) {
        var firstDigit: Char? = null
        var lastDigit: Char? = null
        for ((i, char) in line.withIndex()) {
            if (char in numbers) {
                if (firstDigit == null) firstDigit = char
                lastDigit = char
                continue
            }
            val remainingLetters = line.drop(i)

            wordsToNumbers
                .filter { (word) -> remainingLetters.startsWith(word) }
                .forEach { (_, number) -> if (firstDigit == null) firstDigit = number; lastDigit = number }
        }
        total += "$firstDigit$lastDigit".toInt()
    }

    answer(total)
}