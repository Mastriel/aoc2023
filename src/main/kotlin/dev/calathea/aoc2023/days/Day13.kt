package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.utils.*

private sealed interface Reflection {

    fun questionValue() : Int

    data class Vertical(val column: Int) : Reflection {
        override fun questionValue() = column
    }
    data class Horizontal(val row: Int) : Reflection {
        override fun questionValue() = row * 100
    }

}

private fun FileInput.solveMirror(mirror: Grid<Char>) : List<Reflection> {
    val reflectionCandidates = mutableListOf<Reflection>()
    x@for (x in 0..mirror.sizeX) {
        if (x+1 >= mirror.sizeX) continue
        val column = mirror.columnToString(x)
        val nextColumn = mirror.columnToString(x + 1)
        if (column == nextColumn) {
            var offset = 1
            var colMirrors = true
            while (true) {
                if (x - offset < 0 || x + offset + 1 >= mirror.sizeX) break
                val previousColumn = mirror.columnToString(x - offset)
                val newNextColumn = mirror.columnToString(x + offset + 1)
                if (previousColumn != newNextColumn) {
                    colMirrors = false
                    break
                }
                offset += 1

            }
            if (colMirrors) {
                reflectionCandidates += Reflection.Vertical(x+1)
            }
        }
    }
    y@for (y in 0..mirror.sizeY) {
        if (y+1 >= mirror.sizeY) continue
        val row = mirror.rowToString(y)
        val nextRow = mirror.rowToString(y + 1)
        if (row == nextRow) {
            var offset = 1
            var rowMirrors = true
            while (true) {
                if (y - offset < 0 || y + offset + 1 >= mirror.sizeY) break
                val previousRow = mirror.rowToString(y - offset)
                val newNextRow = mirror.rowToString(y + offset + 1)
                if (previousRow != newNextRow) {
                    rowMirrors = false
                    break
                }

                offset += 1
            }
            if (rowMirrors) {

                reflectionCandidates += Reflection.Horizontal(y+1)
            }
        }
    }


    return reflectionCandidates
}

private fun FileInput.getReflectionLines(mirrors: List<Grid<Char>>) : List<Reflection> {
    val returnMirrors = mutableListOf<Reflection>()
    for (mirror in mirrors) {
        debugPrintln(mirror.stringify())
        solveMirror(mirror).let { returnMirrors += it }
    }
    return returnMirrors
}

val Day13 = Challenge(FileInput("day13.txt")) {
    val mirrors = input.split("\r\n\r\n").map { TextGrid(it) }

    val solvedMirrors = getReflectionLines(mirrors)

    var total = 0
    for (mirror in solvedMirrors) {
        total += when (mirror) {
            is Reflection.Vertical -> mirror.column
            is Reflection.Horizontal -> mirror.questionValue()
        }
    }

    answer(total)
}

val Day13Part2 = Challenge(FileInput("day13.txt")) {
    val mirrors = input.split("\r\n\r\n").map { TextGrid(it) }

    val solvedMirrors = getReflectionLines(mirrors).zip(mirrors)

    var total = 0
    for ((reflection, mirror) in solvedMirrors) {
        var answer : Int? = null
        mirror.forEachBookstyle { pos2D, char ->
            if (answer != null) return@forEachBookstyle
            val newMirror = mirror.clone()
            val flip = if (char == '.') '#' else '.'
            newMirror[pos2D] = flip


            val solved = solveMirror(newMirror)

            if (solved.isNotEmpty() && solved.any { it != reflection }) {
                val new = solved.first { it != reflection }
                debugPrintln(mirror.stringify())
                debugPrintln(newMirror.stringify())
                debugPrintln("Flipping $pos2D makes a new reflection! $new")
                answer = new.questionValue()
            }
        }

        if (answer == null) answer = reflection.questionValue()

        total += answer!!
    }

    answer(total)
}
