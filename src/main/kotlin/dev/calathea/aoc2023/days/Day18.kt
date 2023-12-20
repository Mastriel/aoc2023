package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.utils.Grid
import dev.calathea.aoc2023.utils.Pos2D
import dev.calathea.aoc2023.utils.Pos2DLong
import kotlin.math.abs

private data class MoveInstruction(val move: Pos2D, val color: String)

val Day18 = Challenge(FileInput("day18.txt")) {
    val lines = input.split("\r\n")

    val instructions = getInstructions(lines)

    val gridEntries = mutableListOf<Pos2D>()

    var current = Pos2D.Zero
    gridEntries += Pos2D.Zero

    for (instruction in instructions) {
        current += instruction.move
        gridEntries += current
    }

    val grid = Grid<Boolean>(1000, 1000)
    for (entry in gridEntries) grid[entry] = true

    val insidePos = Pos2D(1, 1)

    grid.floodFill(insidePos, true) { grid[it] == true }


    answer(grid.count { it.value })
}

private fun FileInput.getInstructions(lines: List<String>) : List<MoveInstruction> {
    val instructions = mutableListOf<MoveInstruction>()
    for (line in lines) {
        val (move, amount) = line.split(" ")
        val color = line.split(" ").last().removePrefix("(").removeSuffix(")")
        val direction = when (move.first()) {
            'R' -> Pos2D.Right
            'L' -> Pos2D.Left
            'U' -> Pos2D.Up
            'D' -> Pos2D.Down
            else -> fail("womp womp")
        }
        repeat(amount.toInt()) {
            instructions += MoveInstruction(direction, color)
        }
    }
    return instructions
}

private fun FileInput.getInstructionsPart2(lines: List<String>) : List<MoveInstruction> {
    val instructions = mutableListOf<MoveInstruction>()
    for (line in lines) {
        val color = line.split(" ").last().removePrefix("(#").removeSuffix(")")
        val direction = when (color.last()) {
            '0' -> Pos2D.Right
            '2' -> Pos2D.Left
            '3' -> Pos2D.Up
            '1' -> Pos2D.Down
            else -> fail("womp womp")
        }
        instructions += MoveInstruction(direction * color.take(5).toInt(16), color)
    }
    return instructions
}

fun shoelaceArea(list: List<Pos2DLong>): Long {
    val listSize = list.size
    var acc = 0L
    for (i in 0..<listSize - 1) {
        acc += list[i].x * list[i + 1].y - list[i + 1].x * list[i].y
    }
    return abs(acc + list[listSize - 1].x * list[0].y - list[0].x * list[listSize - 1].y) / 2L
}


val Day18Part2 = Challenge(FileInput("day18.txt")) {
    val lines = input.split("\r\n")

    val instructions = getInstructionsPart2(lines)

    val gridEntries = mutableListOf<Pos2DLong>()

    var current = Pos2D.Zero.toPos2DLong()
    gridEntries += Pos2D.Zero.toPos2DLong()

    for (instruction in instructions) {
        current += instruction.move
        gridEntries += current
    }

    val perimeter = instructions.sumOf { abs(it.move.x + it.move.y) }


    val area = shoelaceArea(gridEntries)
    val points = area - perimeter / 2 + 1

    answer(points + perimeter)

}