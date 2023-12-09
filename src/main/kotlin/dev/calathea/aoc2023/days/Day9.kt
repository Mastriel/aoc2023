package dev.calathea.aoc2023.days


val Day9 = Challenge(FileInput("day9.txt")) {
    val lines = input.split("\r\n")
    val values = lines.map { it.split(" ").map(String::toInt) }

    var total = 0
    for (line in values) {
        var currentValues = line
        val tree = mutableListOf<List<Int>>()

        tree += line

        while (!currentValues.all { it == 0 }) {
            val windowedData = currentValues.windowed(2, 1)
            val nextIter = mutableListOf<Int>()
            for ((first, second) in windowedData) {
                nextIter += second - first
            }
            currentValues = nextIter
            tree += nextIter
        }

        debugPrintln(tree)

        var value = 0
        val reversedTree = tree.withIndex().reversed()

        for ((_, thing) in reversedTree) {
            value += thing.last()
        }

        total += value
    }
    answer(total)
}

val Day9Part2 = Challenge(FileInput("day9.txt")) {
    val lines = input.split("\r\n")
    val values = lines.map { it.split(" ").map(String::toInt) }

    var total = 0
    for (line in values) {
        var currentValues = line
        val tree = mutableListOf<List<Int>>()

        tree += line

        while (!currentValues.all { it == 0 }) {
            val windowedData = currentValues.windowed(2, 1)
            val nextIter = mutableListOf<Int>()
            for ((first, second) in windowedData) {
                nextIter += second - first
            }
            currentValues = nextIter
            tree += nextIter
        }

        debugPrintln(tree)

        var value = 0
        val reversedTree = tree.withIndex().reversed()

        for ((_, treeLine) in reversedTree) {
            debugPrintln("${treeLine.first()} - $value = " + (treeLine.first() - value))
            value = treeLine.first() - value
        }

        total += value
    }
    answer(total)
}