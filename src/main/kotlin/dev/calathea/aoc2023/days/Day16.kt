package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.utils.Grid
import dev.calathea.aoc2023.utils.Pos2D
import dev.calathea.aoc2023.utils.TextGrid
import dev.calathea.aoc2023.utils.stringify


val Day16 = Challenge(FileInput("day16.txt")) {
    val grid = TextGrid(input)

    val tiles = mutableSetOf<Pos2D>()

    val lit = grid.calculateLit(Pos2D(-1, 0), Pos2D.Right)

    answer(lit)
}

fun Grid<Char>.calculateLit(startingPos: Pos2D, startingDir: Pos2D) : Int {
    val nextPositions = mutableSetOf(startingPos to startingDir)

    val totalPositions = mutableSetOf<Pair<Pos2D, Pos2D>>()

    var wasModified = true
    fun addToNext(pos2D: Pos2D, direction: Pos2D) {
        nextPositions += pos2D to direction

        if (!totalPositions.contains(pos2D to direction)) {
            wasModified = true
            totalPositions += pos2D to direction
        }
    }

    while (true) {
        if (!wasModified) break
        wasModified = false
        val copy = nextPositions.toList()
        nextPositions.clear()
        for ((pos, direction) in copy) {
            val nextPos = pos + direction
            when (this[nextPos]) {
                '.' -> {
                    addToNext(nextPos, direction)
                }

                '/' -> {
                    if (direction == Pos2D.Right) {
                        addToNext(nextPos, Pos2D.Up)
                    }
                    if (direction == Pos2D.Left) {
                        addToNext(nextPos, Pos2D.Down)
                    }
                    if (direction == Pos2D.Up) {
                        addToNext(nextPos, Pos2D.Right)
                    }
                    if (direction == Pos2D.Down) {
                        addToNext(nextPos, Pos2D.Left)
                    }
                }

                '\\' -> {
                    if (direction == Pos2D.Right) {
                        addToNext(nextPos, Pos2D.Down)
                    }
                    if (direction == Pos2D.Left) {
                        addToNext(nextPos, Pos2D.Up)
                    }
                    if (direction == Pos2D.Up) {
                        addToNext(nextPos, Pos2D.Left)
                    }
                    if (direction == Pos2D.Down) {
                        addToNext(nextPos, Pos2D.Right)
                    }
                }

                '-' -> {
                    if (direction == Pos2D.Right || direction == Pos2D.Left) {
                        addToNext(nextPos, direction)
                        continue
                    }
                    addToNext(nextPos, Pos2D.Right)
                    addToNext(nextPos, Pos2D.Left)
                }

                '|' -> {
                    if (direction == Pos2D.Up || direction == Pos2D.Down) {
                        addToNext(nextPos, direction)
                        continue
                    }
                    addToNext(nextPos, Pos2D.Up)
                    addToNext(nextPos, Pos2D.Down)
                }
            }
        }
    }


    return totalPositions.distinctBy { it.first }.size
}


val Day16Part2 = Challenge(FileInput("day16.txt")) {
    val grid = TextGrid(input)


    val leftTiles = 0..<grid.sizeX
    val upTiles = 0..<grid.sizeY

    val grids = mutableSetOf<Int>()
    for (tile in leftTiles) {
        grids += grid.calculateLit(Pos2D(-1, tile), Pos2D.Right)
        grids += grid.calculateLit(Pos2D(grid.sizeX, tile), Pos2D.Left)
    }
    for (tile in upTiles) {
        grids += grid.calculateLit(Pos2D(tile, -1), Pos2D.Down)
        grids += grid.calculateLit(Pos2D(tile, grid.sizeY), Pos2D.Up)
    }



    answer(grids.max())
}