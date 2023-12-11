package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.utils.*
import java.awt.Polygon
import kotlin.math.ceil

data class PipeTile(
    val up: Boolean = false,
    val down: Boolean = false,
    val right: Boolean = false,
    val left: Boolean = false
)


val pipes = mapOf(
    '|' to PipeTile(up = true, down = true),
    '-' to PipeTile(right = true, left = true),
    'L' to PipeTile(up = true, right = true),
    'J' to PipeTile(up = true, left = true),
    '7' to PipeTile(left = true, down = true),
    'F' to PipeTile(right = true, down = true),
    'S' to PipeTile(up = true, left = true, right = true, down = true),
    '.' to PipeTile(),
)

val Day10 = Challenge(FileInput("day10.txt")) {
    val grid = TextGrid(input)

    var startingPosition : Pos2D? = null
    for ((pos, char) in grid) {
        if (char == 'S') {
            startingPosition = pos
            break
        }
    }

    println(grid.size)

    val map = mutableMapOf<Pos2D, Int>()
    map[startingPosition!!] = 0
    scanPositions(map, grid, startingPosition)

    val displayGrid = TextGrid(input)


    for ((pos, value) in map) {
        displayGrid[pos] = value.toString().first()
    }
    debugPrintln(displayGrid.stringify())
    debugPrintln(TextGrid(input).stringify())

    answer(ceil(map.toList().maxOf { it.second } / 2.0).toInt())
}


fun FileInput.scanPositions(map: MutableMap<Pos2D, Int>, grid: Grid<Char>, pos: Pos2D) {
    val adjacent = grid.getAdjacent(pos)
    val thisChar = grid[pos]

    val thisCharPipe = pipes[thisChar]!!


    for ((adjacency, adjacentPos) in adjacent) {
        val adjacentChar = grid[adjacentPos] ?: continue
        // debugPrintln("${grid[adjacentPos]} (${adjacentPos}) with adjacency ${adjacency} to ${thisChar} with ${pipes[adjacentChar]}" )
        if (map.keys.contains(adjacentPos)) continue


        val pipeType = pipes[adjacentChar]!!
        // connects
        when (adjacency) {
            AdjacentDirection.Left -> {
                if (thisCharPipe.left && pipeType.right) {
                    map[adjacentPos] = map[pos]!! + 1

                    scanPositions(map, grid, adjacentPos)
                    break
                }
            }
            AdjacentDirection.Right -> {
                if (thisCharPipe.right && pipeType.left) {
                    map[adjacentPos] = map[pos]!! + 1

                    scanPositions(map, grid, adjacentPos)
                    break
                }
            }
            AdjacentDirection.Down -> {
                if (thisCharPipe.down && pipeType.up) {
                    map[adjacentPos] = map[pos]!! + 1

                    scanPositions(map, grid, adjacentPos)
                    break
                }
            }

            AdjacentDirection.Up -> {
                if (thisCharPipe.up && pipeType.down) {
                    map[adjacentPos] = map[pos]!! + 1

                    scanPositions(map, grid, adjacentPos)
                    break
                }
            }
            else -> {}
        }
    }
}

val Day10Part2 = Challenge(FileInput("day10.txt")) {
    val grid = TextGrid(input)

    val list = mutableListOf<Pos2D>()


    var startingPosition : Pos2D? = null
    for ((pos, char) in grid) {
        if (char == 'S') {
            startingPosition = pos
            break
        }
    }

    val map = mutableMapOf<Pos2D, Int>()
    map[startingPosition!!] = 0
    scanPositions(map, grid, startingPosition)

    for ((pos) in grid) {
        if (pos !in map.keys) {
            grid[pos] = '.'
        }
    }

    for ((pos) in grid) {
        if (tileIsEnclosed(grid, pos)) list += pos
    }

    var total = 0
    val debugList = mutableSetOf<Pos2D>()

    val poly = Polygon()

    for ((point) in map) {
        poly.addPoint(point.x, point.y)
    }
    for (point in list) {
        val inside = poly.contains(point.x, point.y)
        if (inside) {
            debugList += point
            total += 1
        }
    }

    for (value in debugList) {
        grid[value] = '#'
    }
    debugPrintln(grid.stringify())

    answer(debugList.size)
}

fun FileInput.tileIsEnclosed(grid: Grid<Char>, pos: Pos2D) : Boolean {
    if (grid[pos]!! != '.') return false

    val traversed = mutableSetOf<Pos2D>()
    traversed += pos

    return recurse(1, traversed, grid, pos)
}


fun FileInput.recurse(depth: Int, traversed: MutableSet<Pos2D>, grid: Grid<Char>, pos: Pos2D, isPartial: Boolean = false) : Boolean {
    if (depth > 10000) return true
    val adjacent = grid.getAdjacent(pos)

    var ret = true
    for ((_, adjacentPos) in adjacent) {
        if (grid[adjacentPos] == null) {
            return false
        }
        if (grid[adjacentPos] != '.') {
            continue
        }
        if (traversed.contains(adjacentPos)) {
            continue
        }
        traversed += adjacentPos
        if (!recurse(depth+1, traversed, grid, adjacentPos)) {
            ret = false
        }
    }
    return ret
}