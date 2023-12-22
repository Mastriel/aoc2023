package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.utils.Grid
import dev.calathea.aoc2023.utils.Pos2D
import dev.calathea.aoc2023.utils.TextGrid
import dev.calathea.aoc2023.utils.stringify
import kotlin.math.pow

val Day21 = Challenge(FileInput("day21.txt")) {
    val grid = TextGrid(input)
    val distance = if (isDebug) 6 else 64


    answer(totalOf(grid, distance) { get(it) })
}

private fun Grid<Char>.getWrapped(pos: Pos2D): Char {
    val stupidSolution = Pos2D(sizeX * 300000, sizeY * 300000)

    return this[((stupidSolution + pos) % (lastPosition + Pos2D(1,1))).abs()]!!
}

fun totalOf(grid: Grid<Char>, amount: Int, getter: Grid<Char>.(Pos2D) -> Char?) : Int {

    val remaining = mutableSetOf<Pos2D>()

    val startingPos = grid.toList().first { it.second == 'S' }.first

    remaining += startingPos
    repeat(amount) {
        for (tile in remaining.toList()) {
            val adjacentTiles = tile.getAdjacent()

            for ((_, pos) in adjacentTiles) {
                val char = getter(grid, pos)

                if (char == '.' || char == 'S') {
                    remaining += pos
                }
            }
            remaining.remove(tile)
        }
    }

    return remaining.size
}

val Day21Part2 = Challenge(FileInput("day21.txt")) {
    val grid = TextGrid(input)

    val hellNumber = 202300L // 26501365 = 202300 * 131 + 65

    val r1 = totalOf(grid, 65) { getWrapped(it) }
    val r2 = totalOf(grid, 131 + 65) { getWrapped(it) }
    val r3 = totalOf(grid, 65 + (2 * 131)) { getWrapped(it) }

    val a = (r3 - (2*r2) + r1) / 2
    val b = ((4*r2) - (3*r1) - r3) / 2

    val squared = a.toLong() * hellNumber.toDouble().pow(2.0).toLong()
    answer(squared + b.toLong()*hellNumber + r1)
}