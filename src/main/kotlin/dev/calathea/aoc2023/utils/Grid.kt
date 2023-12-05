package dev.calathea.aoc2023.utils


data class Pos2D(val x: Int, val y: Int)

class Grid(val sizeX: Int, val sizeY: Int) {
    private val entities = mutableMapOf<Pos2D, GridEntity>()

    operator fun get(pos: Pos2D) : GridEntity {
        return entities[pos] ?: GridEntity.None
    }

    fun forEachBookstyle(block: (Pos2D, GridEntity) -> Unit) {
        repeat(sizeY) { y ->
            repeat(sizeX) { x ->
                val pos = Pos2D(x, y)
                block(pos, get(pos))
            }
        }
    }

    operator fun set(key: Pos2D, value: GridEntity) {
        entities[key] = value
    }
}

fun TextGrid(input: String, mapper: GridTextMapper) : Grid {
    val lines = input.split("\r\n")

    return Grid(lines.first().length, lines.size)
}

sealed interface GridEntity {

    data class Object<T>(val value: T) : GridEntity

    data object None : GridEntity
}


data class GridTextMapper(val pairs: Map<String, GridEntity>) : Map<String, GridEntity> by pairs

fun GridTextMapper(vararg pairs: Pair<String, GridEntity>) = GridTextMapper(mapOf(*pairs))

fun a() {
}