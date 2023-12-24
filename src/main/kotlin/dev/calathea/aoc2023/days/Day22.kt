package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.utils.Pos3D
import dev.calathea.aoc2023.utils.CubeRect3D

private data class SolvedGrid(val settled: List<PosRect3D>, val supported: Int)
private data class PosRect3D(val points: List<Pos3D>) : List<Pos3D> by points {
    fun intersects(other: PosRect3D) : Boolean {
        return this.any { it in other }
    }

    fun lower() : PosRect3D = PosRect3D(this.map { it - Pos3D(0, 0, 1) })
}

private fun FileInput.parse(): List<PosRect3D> {
    return input.split("\r\n")
        .map { line ->
            val (first, second) = line.split("~").map { it.split(",").map(String::toInt) }
            CubeRect3D(Pos3D(first[0], first[1], first[2]), Pos3D(second[0], second[1], second[2])).toPoints()
        }.map(::PosRect3D)
}

private fun settle(rects: List<PosRect3D>): SolvedGrid {
    val newList = mutableListOf<PosRect3D>()
    val settledPositions = mutableListOf<Pos3D>()
    var supportedBricks = 0
    for (rect in rects.sortedBy { it.minOf { r -> r.z } }) {
        var lastGood = rect

        while (true) {
            val lowered = lastGood.lower()

            if (lowered.any { it.z <= 0 || it in settledPositions }) {
                newList += lastGood
                settledPositions += lastGood
                if (lastGood != rect) supportedBricks += 1
                break
            }

            lastGood = lowered
        }
    }
    return SolvedGrid(newList, supportedBricks)
}


val Day22 = Challenge(FileInput("day22.txt")) {
    val rectangles = parse()

    val settled = settle(rectangles).settled

    val supports = settled.map {
        val newList = settled.minusElement(it)
        settle(newList).supported
    }

    answer(supports.count { it == 0 })


}


val Day22Part2 = Challenge(FileInput("day22.txt")) {

    val rectangles = parse()

    val settled = settle(rectangles).settled

    val supports = settled.map {
        val newList = settled.minusElement(it)
        settle(newList).supported
    }

    answer(supports.sum())
}