package dev.calathea.aoc2023.utils

data class Pos3D(val x: Int, val y: Int, val z: Int) {
    operator fun minus(other: Pos3D) = Pos3D(x - other.x, y - other.y, z - other.z)
}

data class CubeRect3D(val start: Pos3D, val end: Pos3D) {
    operator fun contains(pos3D: Pos3D) : Boolean {
        return pos3D.x in start.x..end.x &&
            pos3D.y in start.y..end.y &&
            pos3D.z in start.z..end.z
    }

    fun toPoints() : List<Pos3D> {
        return buildList {
            for (x in start.x..end.x) {
                for (y in start.y..end.y) {
                    for (z in start.z..end.z) {
                        add(Pos3D(x, y, z))
                    }
                }
            }
        }
    }

    operator fun minus(pos: Pos3D) = CubeRect3D(start - pos, end - pos)

    fun intersects(other: CubeRect3D) : Boolean {
        return !(other.start.x > this.start.x + this.end.x || other.start.x+other.end.x < this.start.x ||
                other.start.y > this.start.y + this.end.y || other.start.y+other.end.y < this.start.y ||
                other.start.z > this.start.z + this.end.z || other.start.z+other.end.z < this.start.z)
    }
}