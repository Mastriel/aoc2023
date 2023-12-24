package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.utils.Pos2D
import dev.calathea.aoc2023.utils.Pos2DL
import dev.calathea.aoc2023.utils.Pos3D
import kotlin.math.absoluteValue

private data class MovingHail2D(val position: Pos2DL, val direction: Pos2DL)

private data class Pos2DD(val x: Double, val y: Double)

private const val COLLISION_MIN = 200000000000000
private const val COLLISION_MAX = 400000000000000
private fun FileInput.parse() : List<MovingHail2D> {

    return input.split("\r\n").map {
        val (pos, vel) = it.split(" @ ").map { r -> r.split(", ").map { c -> c.trim().toLong() } }
        MovingHail2D(Pos2DL(pos[0], pos[1]), Pos2DL(vel[0], vel[1]))
    }
}

private fun rayIntersection(
    a: MovingHail2D,
    b: MovingHail2D
): Pos2DD? {

    val (x1, y1) = a.position.x.toDouble() to a.position.y.toDouble()
    val (dx1, dy1) = a.direction.x.toDouble() to a.direction.y.toDouble()
    val (x2, y2) = b.position.x.toDouble() to b.position.y.toDouble()
    val (dx2, dy2) = b.direction.x.toDouble() to b.direction.y.toDouble()


    val m1 = dy1 / dx1
    val m2 = dy2 / dx2

    if ((m2 - m1).absoluteValue < 0.0000000001) return null

    val x = (m1 * x1 - m2*x2 + y2 - y1) / (m1 - m2)
    val y = (m1*m2*(x2-x1) + m2*y1 - m1*y2) / (m2 - m1)

    if ((dx1 < 0.0 && x > x1) || (dx1 > 0.0 && x < x1)) return null
    if ((dx2 < 0.0 && x > x2) || (dx2 > 0.0 && x < x2)) return null

    return Pos2DD(x, y)
}

val Day24 = Challenge(FileInput("day24.txt")) {
    val hail = parse()

    val (min, max) = if (isDebug) 7L to 27L else COLLISION_MIN to COLLISION_MAX
    val dRange = min.toDouble()..max.toDouble()

    val positions = mutableListOf<Pos2DD>()
    val tested = mutableSetOf<Pair<MovingHail2D, MovingHail2D>>()

    for (a in hail) {
        for (b in hail) {
            if (a to b in tested || b to a in tested) continue
            if (a == b) {
                continue
            }

            try {
                tested += a to b

                val point = rayIntersection(a, b)
                debugPrintln(point)
                if (point != null && point.x in dRange && point.y in dRange) {
                    positions += point
                    debugPrintln("${a.position} intersects ${b.position} at (${point.x}, ${point.y})")
                }
            } catch (e: ArithmeticException) {
                debugPrintln("${a.position} DOES NOT intersect ${b.position}")
            }

        }
    }
    debugPrintln(positions.filter { it.x in dRange && it.y in dRange })
    answer(positions.size)
}

val Day24Part2 = Challenge(FileInput("day24.txt")) {
    answer("Done in Python, see /Day24Part2.py. Requires z3 and z3-solver to be installed. Put day24.txt in the same directory as the script.")
}