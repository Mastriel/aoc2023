package dev.calathea.aoc2023.utils

fun lcm(a: Int, b: Int): Int {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0 && lcm % b == 0) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun lcm(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}


fun List<Int>.lcm(): Int {
    var result = this[0]
    for (i in 1..<this.size) {
        result = lcm(result, this[i])
    }
    return result
}

fun List<Long>.lcm(): Long {
    var result = this[0]
    for (i in 1..<this.size) {
        result = lcm(result, this[i])
    }
    return result
}