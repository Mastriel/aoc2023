package dev.calathea.aoc2023.utils

import java.awt.Point
import kotlin.math.abs


data class Pos2D(val x: Int, val y: Int) {
    operator fun minus(other: Pos2D) : Pos2D {
        return Pos2D(x - other.x, y - other.y)
    }

    operator fun plus(other: Pos2D) : Pos2D {
        return Pos2D(x + other.x, y + other.y)
    }

    operator fun times(other: Pos2D) : Pos2D {
        return Pos2D(x * other.x, y * other.y)
    }

    operator fun times(other: Int) : Pos2D {
        return Pos2D(x * other, y * other)
    }

    fun distanceTo(other: Pos2D) : Double {
        return Point(x, y).distance(other.x.toDouble(), other.y.toDouble())
    }

    fun manhattanDistanceTo(other: Pos2D) : Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    fun toPos2DLong() = Pos2DLong(x.toLong(), y.toLong())

    fun getAdjacent(withCorners: Boolean = false) : Map<AdjacentDirection, Pos2D> {
        val map = mutableMapOf<AdjacentDirection, Pos2D>()

        map[AdjacentDirection.Left] = AdjacentDirection.Left.offset(this)
        map[AdjacentDirection.Right] = AdjacentDirection.Right.offset(this)
        map[AdjacentDirection.Up] = AdjacentDirection.Up.offset(this)
        map[AdjacentDirection.Down] = AdjacentDirection.Down.offset(this)
        if (withCorners) {
            map[AdjacentDirection.TopLeft] = AdjacentDirection.TopLeft.offset(this)
            map[AdjacentDirection.TopRight] = AdjacentDirection.TopRight.offset(this)
            map[AdjacentDirection.BottomLeft] = AdjacentDirection.BottomLeft.offset(this)
            map[AdjacentDirection.BottomRight] = AdjacentDirection.BottomRight.offset(this)
        }
        return map
    }

    fun mirror() : Pos2D = Pos2D(-x, -y)

    companion object {
        val Up = Pos2D(0, -1)
        val Down = Pos2D(0, 1)
        val Right = Pos2D(1, 0)
        val Left = Pos2D(-1, 0)
        val Zero = Pos2D(0, 0)

        val Directions = listOf(Up, Left, Down, Right)
    }
}

data class Pos2DLong(val x: Long, val y: Long) {
    operator fun minus(other: Pos2DLong) : Pos2DLong {
        return Pos2DLong(x - other.x, y - other.y)
    }

    operator fun plus(other: Pos2DLong) : Pos2DLong {
        return Pos2DLong(x + other.x, y + other.y)
    }

    operator fun minus(other: Pos2D) : Pos2DLong {
        return Pos2DLong(x - other.x, y - other.y)
    }

    operator fun plus(other: Pos2D) : Pos2DLong {
        return Pos2DLong(x + other.x, y + other.y)
    }

    fun manhattanDistanceTo(other: Pos2DLong) : Long {
        return abs(x - other.x) + abs(y - other.y)
    }

    fun getAdjacent(withCorners: Boolean = false) : Map<AdjacentDirection, Pos2DLong> {
        val map = mutableMapOf<AdjacentDirection, Pos2DLong>()

        map[AdjacentDirection.Left] = this + AdjacentDirection.Left.posOffset
        map[AdjacentDirection.Right] = this + AdjacentDirection.Right.posOffset
        map[AdjacentDirection.Up] = this + AdjacentDirection.Up.posOffset
        map[AdjacentDirection.Down] = this + AdjacentDirection.Down.posOffset
        if (withCorners) {
            map[AdjacentDirection.TopLeft] = this + AdjacentDirection.TopLeft.posOffset
            map[AdjacentDirection.TopRight] = this + AdjacentDirection.TopRight.posOffset
            map[AdjacentDirection.BottomLeft] = this + AdjacentDirection.BottomLeft.posOffset
            map[AdjacentDirection.BottomRight] = this + AdjacentDirection.BottomRight.posOffset
        }
        return map
    }
}

sealed class AdjacentDirection(val posOffset: Pos2D) {
    data object Left : AdjacentDirection(Pos2D.Left)
    data object Right : AdjacentDirection(Pos2D.Right)
    data object Up : AdjacentDirection(Pos2D.Up)
    data object Down : AdjacentDirection(Pos2D.Down)

    data object TopLeft : AdjacentDirection(Pos2D(-1, -1))
    data object TopRight : AdjacentDirection(Pos2D(1, -1))
    data object BottomLeft : AdjacentDirection(Pos2D(-1, 1))
    data object BottomRight : AdjacentDirection(Pos2D(1, 1))

    fun offset(pos: Pos2D) = pos + posOffset
}

/**
 * [T] should be immutable, otherwise [clone] will mess up.
 */
class Grid<T: Any>(var sizeX: Int = Int.MAX_VALUE, var sizeY: Int = Int.MAX_VALUE) : MutableMap<Pos2D, T> by mutableMapOf(), Cloneable {

    fun <N: Any> remap(transformer: (Pos2D, T) -> N) : Grid<N> {
        return Grid<N>(sizeX, sizeY).also {
            this.forEachBookstyle { pos2D, item ->
                it[pos2D] = transformer(pos2D, item)
            }
        }
    }

    fun fill(item: (Pos2D) -> T) {
        this.forEachBookstyle { pos2D, _ ->
            this[pos2D] = item(pos2D)
        }
    }

    fun floodFill(pos: Pos2D, item: T, wallCondition: (Pos2D) -> Boolean) {
        val adjacent = getAdjacentWithValues(pos).values

        for (adjacentPieces in adjacent) {
            if (wallCondition(adjacentPieces.first) || adjacentPieces.second == item) continue

            this[adjacentPieces.first] = item
            floodFill(adjacentPieces.first, item, wallCondition)
        }
    }

    public override fun clone() : Grid<T> {
        return Grid<T>(sizeX, sizeY).also {
            this.forEachBookstyle { pos2D, item ->
                it[pos2D] = item
            }
        }
    }

    fun forEachBookstyle(block: (Pos2D, T) -> Unit) {
        val positions = this.keys
        val items = positions.sortedBy { it.y }.sortedBy { it.x }
        for (pos in items) {
            block(pos, get(pos)!!)
        }
    }

    fun getAdjacent(pos: Pos2D, withCorners: Boolean = false) : Map<AdjacentDirection, Pos2D> {
        return pos.getAdjacent(withCorners)
    }

    operator fun contains(pos: Pos2D) : Boolean {
        return pos.x >= 0 && pos.y >= 0 && pos.x < sizeX && pos.y < sizeY
    }

    fun getAdjacentWithValues(pos: Pos2D, withCorners: Boolean = false) : Map<AdjacentDirection, Pair<Pos2D, T?>> {
        val map = mutableMapOf<AdjacentDirection, Pos2D>()

        map[AdjacentDirection.Left] = AdjacentDirection.Left.offset(pos)
        map[AdjacentDirection.Right] = AdjacentDirection.Right.offset(pos)
        map[AdjacentDirection.Up] = AdjacentDirection.Up.offset(pos)
        map[AdjacentDirection.Down] = AdjacentDirection.Down.offset(pos)
        if (withCorners) {
            map[AdjacentDirection.TopLeft] = AdjacentDirection.TopLeft.offset(pos)
            map[AdjacentDirection.TopRight] = AdjacentDirection.TopRight.offset(pos)
            map[AdjacentDirection.BottomLeft] = AdjacentDirection.BottomLeft.offset(pos)
            map[AdjacentDirection.BottomRight] = AdjacentDirection.BottomRight.offset(pos)
        }
        return map.mapValues { it.value to this[it.value] }
    }

}

fun Grid<Char>.stringify() : String {
    return buildString {
        repeat(sizeY) { y ->
            repeat(sizeX) { x ->
                append(this@stringify[Pos2D(x, y)] ?: '?')
            }
            append("\n")
        }
    }
}

fun Grid<Char>.columnToString(x: Int) : String {
    return buildString {
        repeat(sizeY) {
            append(this@columnToString[Pos2D(x, it)] ?: "")
        }
    }
}

fun Grid<Char>.rowToString(y: Int) : String {
    return buildString {
        repeat(sizeX) {
            append(this@rowToString[Pos2D(it, y)] ?: "")
        }
    }
}

fun TextGrid(input: String) : Grid<Char> {
    val lines = input.split("\r\n")

    return Grid<Char>(lines.first().length, lines.size).also {
        repeat(lines.first().length) { y ->
            repeat(lines.size) { x ->
                val pos = Pos2D(y, x)
                it[pos] = lines[x][y]
            }
        }
    }
}