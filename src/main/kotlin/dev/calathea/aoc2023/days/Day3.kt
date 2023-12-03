package dev.calathea.aoc2023.days

private fun Char?.isSymbol() : Boolean = this != null && !this.isDigit() && this != '.'

private fun Char?.isGear() : Boolean = this == '*'

val Day3 = Challenge(FileInput("day3.txt")) {
    val lines = input.split("\r\n")

    var total = 0
    for ((lineIndex, line) in lines.withIndex()) {

        val ignoredIndexes = mutableListOf<Int>()
        for ((charIndex) in line.withIndex()) {
            if (charIndex in ignoredIndexes) {
                continue
            }
            var number = ""
            var isValidNumber = false
            var currentIndex = charIndex
            w@while (true) {
                val currentChar = line.getOrNull(currentIndex) ?: break
                if (currentChar.isDigit()) {
                    val aboveLine = lines.getOrNull(lineIndex-1)
                    val belowLine = lines.getOrNull(lineIndex+1)
                    if (
                        line.getOrNull(currentIndex-1)?.isSymbol() == true ||
                        line.getOrNull(currentIndex+1)?.isSymbol() == true ||
                        aboveLine?.getOrNull(currentIndex-1)?.isSymbol() == true ||
                        aboveLine?.getOrNull(currentIndex)?.isSymbol() == true ||
                        aboveLine?.getOrNull(currentIndex+1)?.isSymbol() == true ||
                        belowLine?.getOrNull(currentIndex-1)?.isSymbol() == true ||
                        belowLine?.getOrNull(currentIndex)?.isSymbol() == true ||
                        belowLine?.getOrNull(currentIndex+1)?.isSymbol() == true
                    ) {
                        debugPrintln(
                            (line.getOrNull(currentIndex-1)?.code).toString() + ", " +
                            (line.getOrNull(currentIndex+1)?.code) + ", " +
                            (aboveLine?.getOrNull(currentIndex-1)?.code) + ", " +
                            (aboveLine?.getOrNull(currentIndex)?.code) + ", " +
                            (aboveLine?.getOrNull(currentIndex+1)?.code) + ", " +
                            (belowLine?.getOrNull(currentIndex-1)?.code) + ", " +
                            (belowLine?.getOrNull(currentIndex)?.code) + ", " +
                            (belowLine?.getOrNull(currentIndex+1)?.code)
                        )
                        debugPrintln("$lineIndex:$currentIndex is a valid number")
                        isValidNumber = true
                    }
                    ignoredIndexes += currentIndex
                    number += currentChar
                    currentIndex++
                } else {
                    break@w
                }
            }
            if (isValidNumber) {
                debugPrintln("Added $lineIndex:$charIndex (${number})")
                total += number.toInt()
            }
        }
    }
    answer(total)
}

val Day3Part2 = Challenge(FileInput("day3.txt")) {
    val lines = input.split("\r\n")

    var total = 0

    val map = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
    for ((lineIndex, line) in lines.withIndex()) {
        val ignoredIndexes = mutableListOf<Int>()
        for ((charIndex) in line.withIndex()) {
            if (charIndex in ignoredIndexes) {
                continue
            }
            var number = ""
            var currentIndex = charIndex
            val gearLocations : MutableList<Pair<Int, Int>> = mutableListOf()
            w@while (true) {
                val currentChar = line.getOrNull(currentIndex) ?: break@w
                if (currentChar.isDigit()) {
                    val aboveLine = lines.getOrNull(lineIndex-1)
                    val belowLine = lines.getOrNull(lineIndex+1)

                    if (line.getOrNull(currentIndex-1)?.isGear() == true) gearLocations += lineIndex to (currentIndex-1)
                    if (line.getOrNull(currentIndex+1)?.isGear() == true) gearLocations += lineIndex to (currentIndex+1)

                    if (aboveLine?.getOrNull(currentIndex-1)?.isGear() == true) gearLocations += lineIndex-1 to (currentIndex-1)
                    if (aboveLine?.getOrNull(currentIndex)?.isGear() == true) gearLocations += lineIndex-1 to (currentIndex)
                    if (aboveLine?.getOrNull(currentIndex+1)?.isGear() == true) gearLocations += lineIndex-1 to (currentIndex+1)

                    if (belowLine?.getOrNull(currentIndex-1)?.isGear() == true) gearLocations += lineIndex+1 to (currentIndex-1)
                    if (belowLine?.getOrNull(currentIndex)?.isGear() == true) gearLocations += lineIndex+1 to (currentIndex)
                    if (belowLine?.getOrNull(currentIndex+1)?.isGear() == true) gearLocations += lineIndex+1 to (currentIndex+1)

                    ignoredIndexes += currentIndex
                    number += currentChar
                    currentIndex++
                } else {
                    break@w
                }
            }
            if (gearLocations.isNotEmpty()) {
                val distinctLocations = gearLocations.distinct()

                if (distinctLocations.size != 1) {
                    fail("Too many gear locations for 1 number")
                }
                for (location in distinctLocations) {
                    val list = map[location] ?: run {
                        val newList = mutableListOf<Int>()
                        map[location] = newList
                        newList
                    }
                    list += number.toInt()
                }
            }
        }
    }

    for ((key, value) in map) {
        debugPrintln("$key has values $value")

        if (value.size != 2) {
            debugPrintln("List too big/small!")
            continue
        }
        val thing = value.reduce { acc, it -> acc * it }
        debugPrintln(thing)
        total += thing
    }


    answer(total)
}