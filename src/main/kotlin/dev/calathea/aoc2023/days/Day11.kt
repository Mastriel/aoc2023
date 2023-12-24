package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.utils.*

private fun FileInput.fixGrid(amount: Int) : List<Pos2D> {
    val naiveGrid = TextGrid(input)

    val columnInserts = mutableListOf<Int>()
    val rowInserts = mutableListOf<Int>()

    for (column in 0..naiveGrid.sizeX) {
        var containsGalaxy = false
        for (row in 0..naiveGrid.sizeY) {
            val pos = Pos2D(row, column)
            if (naiveGrid[pos] == '#') {
                containsGalaxy = true
                break
            }
        }
        if (!containsGalaxy) {
            columnInserts += column
        }
    }
    for (row in 0..naiveGrid.sizeY) {
        var containsGalaxy = false
        for (column in 0..naiveGrid.sizeX) {
            val pos = Pos2D(row, column)
            if (naiveGrid[pos] == '#') {
                containsGalaxy = true
                break
            }
        }
        if (!containsGalaxy) {
            rowInserts += row
        }
    }

    val galaxies = naiveGrid.filter { it.value == '#' }
    val newGalaxies = mutableListOf<Pos2D>()
    for ((pos) in galaxies) {
        val offsetY = columnInserts.filter { it < pos.y }.size * amount
        val offsetX = rowInserts.filter { it < pos.x }.size * amount
        newGalaxies += Pos2D(offsetX + pos.x, offsetY + pos.y)
    }

    return newGalaxies
}

val Day11 = Challenge(FileInput("day11.txt")) {

    val galaxies = fixGrid(1)

    val alreadyDone = mutableListOf<Pair<Pos2D, Pos2D>>()

    var total = 0
    var totalPairs = 0

    debugPrintln(galaxies)
    for (pos1 in galaxies) {
        for (pos2 in galaxies) {
            if (alreadyDone.contains(pos2 to pos1) || alreadyDone.contains(pos2 to pos1))
                continue
            if (pos1 == pos2) continue
            totalPairs += 1

            total += pos1.manhattanDistanceTo(pos2)
            alreadyDone += pos1 to pos2
        }
    }
    debugPrintln(totalPairs)
    answer(total)
}


val Day11Part2 = Challenge(FileInput("day11.txt")) {
    val galaxies = fixGrid(999_999).map { it.toPos2DLong() }

    val alreadyDone = mutableListOf<Pair<Pos2DL, Pos2DL>>()

    var total = 0L
    var totalPairs = 0

    for (pos1 in galaxies) {
        for (pos2 in galaxies) {
            if (alreadyDone.contains(pos2 to pos1) || alreadyDone.contains(pos2 to pos1))
                continue
            if (pos1 == pos2) continue
            totalPairs += 1

            total += pos1.manhattanDistanceTo(pos2)
            alreadyDone += pos1 to pos2
        }
    }
    answer(total)
}