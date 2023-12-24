package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.utils.AdjacentDirection
import dev.calathea.aoc2023.utils.Pos2D
import dev.calathea.aoc2023.utils.TextGrid
import dev.calathea.aoc2023.utils.stringify

private data class Path(val tiles: MutableList<Pos2D> = mutableListOf()) : MutableList<Pos2D> by tiles

val Day23 = Challenge(FileInput("day23.txt")) {
    val grid = TextGrid(input)
    val startingPos = grid.toList().filter { it.first.y == 0 }.first { it.second == '.' }.first

    var remaining = mutableListOf(Path(mutableListOf(startingPos)))

    val completedPaths = mutableSetOf<Path>()

    while (remaining.isNotEmpty()) {
        debugPrintln(remaining)
        path@for (path in remaining.toList()) {
            val head = path.last()
            val adjacent = head.getAdjacent().filter {
                when (grid[head]) {
                    '<' -> it.key == AdjacentDirection.Left
                    'v' -> it.key == AdjacentDirection.Down
                    '>' -> it.key == AdjacentDirection.Right
                    '^' -> it.key == AdjacentDirection.Up
                    else -> true
                }
            }


            val newPosibilities = mutableListOf<Pos2D>()
            for ((adjacency, pos) in adjacent) {
                val char = grid[pos]


                if (adjacency == AdjacentDirection.Left  && char == '>') continue
                if (adjacency == AdjacentDirection.Right && char == '<') continue
                if (adjacency == AdjacentDirection.Up    && char == 'v') continue
                if (adjacency == AdjacentDirection.Down  && char == '^') continue

                if (char != null && char != '#' && pos !in path) newPosibilities += pos

                if (pos != startingPos && pos.y == grid.sizeY) {
                    println("Found a path: ${path.size}")
                    remaining -= path
                    path += pos
                    completedPaths += path
                    continue@path
                }
            }

            debugPrintln(newPosibilities)
            for (newPos in newPosibilities.drop(1)) {
                remaining += Path(path.toMutableList()).also { it += newPos }
            }
            newPosibilities.firstOrNull()?.let { path += it }

            if (newPosibilities.isEmpty()) {
                debugPrintln("Path pruned: $path")
                remaining.remove(path)
                continue@path
            }
        }
        val newRemaining = mutableListOf<Path>()
        for ((_, pathList) in remaining.groupBy { it.tiles.last() to it.size }) {
            val rootPath = pathList.first()
            for (path in pathList.drop(1)) rootPath += path
            newRemaining += Path(rootPath.distinct().toMutableList())
        }
        remaining = newRemaining
    }

    debugPrintln(completedPaths.map { it.size - 2 })

    answer(completedPaths.maxOf { it.size - 2})
}

// absurdly slow. you don't want to know what deals i had to make and favors i had
// to cash in to make this actually work
val Day23Part2 = Challenge(FileInput("day23.txt")) {
    val grid = TextGrid(input)
    val startingPos = grid.toList().filter { it.first.y == 0 }.first { it.second == '.' }.first

    for ((pos, char) in grid) {
        if (char in "<>^v") grid[pos] = '.'
    }

    val remaining = mutableListOf(Path(mutableListOf(startingPos)))


    var total = 0
    while (remaining.isNotEmpty()) {
        path@for (path in remaining.toList()) {
            val head = path.last()
            val adjacent = head.getAdjacent()

            val newPosibilities = mutableListOf<Pos2D>()
            for ((_, pos) in adjacent) {
                val char = grid[pos]


                if (char != null && char != '#' && pos !in path) newPosibilities += pos

                if (pos != startingPos && pos.y == grid.sizeY) {
                    remaining -= path
                    if (path.size > total) {
                        total = path.size
                    }
                    continue@path
                }
            }

            for (newPos in newPosibilities.drop(1)) {
                remaining += Path(path.toMutableList()).also { it += newPos }
            }
            newPosibilities.firstOrNull()?.let { path += it }

            if (newPosibilities.isEmpty()) {
                remaining.remove(path)
                continue@path
            }
        }

//        val newRemaining = mutableListOf<Path>()
//        for ((_, pathList) in remaining.groupBy { it.tiles.last() to it.size }) {
//            val rootPath = pathList.first()
//            for (path in pathList.drop(1)) rootPath += path
//            newRemaining += Path(rootPath.distinct().toMutableList())
//        }
//        remaining = newRemaining
    }

    answer(total - 1)
}