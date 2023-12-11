package dev.calathea.aoc2023.utils

import com.sun.org.apache.xpath.internal.operations.Bool
import java.awt.Point
import kotlin.math.abs


data class Pos2D(val x: Int, val y: Int) {
    operator fun minus(other: Pos2D) : Pos2D {
        return Pos2D(x - other.x, y - other.y)
    }

    operator fun plus(other: Pos2D) : Pos2D {
        return Pos2D(x + other.x, y + other.y)
    }

    fun distanceTo(other: Pos2D) : Double {
        return Point(x, y).distance(other.x.toDouble(), other.y.toDouble())
    }

    fun manhattanDistanceTo(other: Pos2D) : Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    fun toPos2DLong() = Pos2DLong(x.toLong(), y.toLong())
}

data class Pos2DLong(val x: Long, val y: Long) {
    operator fun minus(other: Pos2DLong) : Pos2DLong {
        return Pos2DLong(x - other.x, y - other.y)
    }

    operator fun plus(other: Pos2DLong) : Pos2DLong {
        return Pos2DLong(x + other.x, y + other.y)
    }

    fun manhattanDistanceTo(other: Pos2DLong) : Long {
        return abs(x - other.x) + abs(y - other.y)
    }
}

sealed class AdjacentDirection(val posOffset: Pos2D) {
    data object Left : AdjacentDirection(Pos2D(-1, 0))
    data object Right : AdjacentDirection(Pos2D(1, 0))
    data object Up : AdjacentDirection(Pos2D(0, -1))
    data object Down : AdjacentDirection(Pos2D(0, 1))

    data object TopLeft : AdjacentDirection(Pos2D(-1, -1))
    data object TopRight : AdjacentDirection(Pos2D(1, -1))
    data object BottomLeft : AdjacentDirection(Pos2D(-1, 1))
    data object BottomRight : AdjacentDirection(Pos2D(1, 1))

    fun offset(pos: Pos2D) = pos + posOffset
}

/**
 * [T] should be immutable, otherwise [clone] will mess up.
 */
class Grid<T: Any>(var sizeX: Int, var sizeY: Int) : MutableMap<Pos2D, T> by mutableMapOf(), Cloneable {

    fun <N: Any> remap(transformer: (Pos2D, T) -> N) : Grid<N> {
        return Grid<N>(sizeX, sizeY).also {
            this.forEachBookstyle { pos2D, item ->
                it[pos2D] = transformer(pos2D, item)
            }
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
        repeat(sizeY) { y ->
            repeat(sizeX) { x ->
                val pos = Pos2D(x, y)
                block(pos, get(pos)!!)
            }
        }
    }

    fun getAdjacent(pos: Pos2D, withCorners: Boolean = false) : Map<AdjacentDirection, Pos2D> {
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
        return map
    }
}

fun Grid<Char>.stringify() : String {
    return buildString {
        repeat(sizeY) { y ->
            repeat(sizeX) { x ->
                append(this@stringify[Pos2D(x, y)])
            }
            append("\n")
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