package dev.calathea.aoc2023.utils



fun IntRange.size() : Int {
    return (last - start) + 1
}

infix fun IntRange.combinedWith(other: IntRange) = (maxOf(first, other.first)..minOf(last, other.last))
