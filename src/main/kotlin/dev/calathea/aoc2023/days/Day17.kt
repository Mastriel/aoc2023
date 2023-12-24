package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.utils.*
import java.util.PriorityQueue

val Day17 = Challenge(FileInput("day17.txt")) {
    val grid = TextGrid(input).remap { _, c -> c.digitToInt() }

    val total = listOf(
        minPath(grid, 0, 3, Pos2D.Right),
        minPath(grid, 0, 3, Pos2D.Down)
    ).min()
    answer(total)
}

// partially taken from https://github.com/nothingelsematters/contests/blob/master/advent-of-code/2023/src/Day17.kt
// i do not know what a dijkstra is.
private fun FileInput.minPath(grid: Grid<Int>, minRun: Int, maxRun: Int, initialDirection: Pos2D): Int {
    val endPos = Pos2D(grid.sizeX - 1, grid.sizeY - 1)

    val queue = PriorityQueue<Pair<State, Int>>(compareBy { it.second })
    queue += State(Pos2D.Zero, 0, initialDirection) to 0
    val visited = mutableSetOf<State>()


    while (queue.isNotEmpty()) {
        val (state, distance) = queue.poll()
        if (state in visited) continue
        if (state.position == endPos) return distance

        visited += state

        val sequence = sequence {
            if (state.run >= minRun) {
                val turn = Pos2D.Directions.asSequence()
                    .filter { it != state.direction && it != state.direction.mirror() }
                    .map { it to 1 }
                yieldAll(turn)
            }
            if (state.run < maxRun) {
                yield(state.direction to state.run + 1)
            }
        }

        sequence.filter { (pos) -> state.position + pos in grid }
            .forEach { (direction, run) ->
                val newPos = state.position + direction
                queue += State(newPos, run, direction) to (distance + grid[newPos]!!)
            }
    }
    fail("No path found :(")
}

private data class State(val position: Pos2D, val run: Int, val direction: Pos2D)


val Day17Part2 = Challenge(FileInput("day17.txt")) {
    val grid = TextGrid(input).remap { _, c -> c.digitToInt() }

    val total = listOf(
        minPath(grid, 4, 10, Pos2D.Right),
        minPath(grid, 4, 10, Pos2D.Down)
    ).min()
    answer(total)
}