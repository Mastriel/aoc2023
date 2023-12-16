package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.utils.Grid
import dev.calathea.aoc2023.utils.Pos2D
import dev.calathea.aoc2023.utils.TextGrid
import dev.calathea.aoc2023.utils.stringify

fun FileInput.tiltGrid(grid: Grid<Char>, direction: Pos2D = Pos2D.Up) {
    while (true) {
        var somethingHappened = false
        grid.forEachBookstyle { pos2D, c ->
            if (c == 'O') {
                val newPos = pos2D + direction

                val newTile = grid[newPos]
                if (newTile == '.') {
                    somethingHappened = true
                    grid[newPos] = 'O'
                    grid[pos2D] = '.'
                }
            }
        }
        if (!somethingHappened) break
    }
}

fun FileInput.spinGrid(grid: Grid<Char>) : Grid<Char> {
    val positions = mutableListOf<Grid<Char>>()

    for (i in 0..1_000_000_000) {
        for (direction in Pos2D.Directions) {
            tiltGrid(grid, direction)
        }
        if (i % 1_000 == 0) debugPrintln("Passed $i iterations")
        if (positions.any { it.stringify() == grid.stringify() }) {
            val gridIndex = positions.indexOfFirst { it.stringify() == grid.stringify() }
            debugPrintln("Grid below repeated at $i (found at $gridIndex, distance ${i - gridIndex})")
            val distance = i - gridIndex

            val remaining = (1_000_000_000 - i) % distance - 1

            debugPrintln("Running another ${remaining} cycles")

            for (i2 in 0..<remaining) {
                for (direction in Pos2D.Directions) {
                    tiltGrid(grid, direction)
                }
            }

            return grid
        }

        positions += grid.clone()
    }
    error("how")
}

val Day14 = Challenge(FileInput("day14.txt")) {
    val grid = TextGrid(input)
    tiltGrid(grid)

    var total = 0
    debugPrintln(grid.stringify())
    for (rock in grid.filter { it.value == 'O' }) {
        total += grid.sizeY - rock.key.y


    }
    answer(total)
}


val Day14Part2 = Challenge(FileInput("day14.txt")) {
    var grid = TextGrid(input)
    grid = spinGrid(grid)

    var total = 0
    for (rock in grid.filter { it.value == 'O' }) {
        total += grid.sizeY - rock.key.y
    }
    answer(total)
}