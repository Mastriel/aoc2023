package dev.calathea.aoc2023.days

data class Race(val distance: Int, val time: Int)
data class RaceLong(val distance: Long, val time: Long)

val Day6 = Challenge(FileInput("day6.txt")) {
    val lines = input.split("\r\n")
    val times = lines.first().split(": ").last().split(" ").filter { it.isNotEmpty() }
    val distance = lines.last().split(": ").last().split(" ").filter { it.isNotEmpty() }
    val races = mutableListOf<Race>()
    for ((i, time) in times.withIndex()) {
        races.add(Race(distance[i].toInt(), time.toInt()))
    }

    var recordBeaters = mutableMapOf<Race, Int>()
    for (race in races) {
        repeat(race.time) { windupTime ->
            if (windupTime == 0) return@repeat

            val newDistance = (race.time - windupTime) * windupTime
            if (newDistance > race.distance) {
                recordBeaters.putIfAbsent(race, 0)
                recordBeaters[race] = recordBeaters[race]!! + 1
            }
        }
    }

    answer(recordBeaters.values.reduce { acc, it -> acc * it })
}

val Day6Part2 = Challenge(FileInput("day6.txt")) {
    val lines = input.split("\r\n")
    val time = lines.first().split(": ").last().split(" ").filter { it.isNotEmpty() }.joinToString("")
    val distance = lines.last().split(": ").last().split(" ").filter { it.isNotEmpty() }.joinToString("")

    val race = RaceLong(distance.toLong(), time.toLong())

    debugPrintln(race)

    var start = 0L
    var end = 0L
    for (windupTime in 1..race.time.toInt()) {

        val raceDistance = (race.time - windupTime) * windupTime
        if (raceDistance > race.distance) {
            debugPrintln("Beaten 1!")
            start = windupTime.toLong()
            break
        }
    }
    for (windupTime in race.time.toInt() downTo 1) {

        val raceDistance = (race.time - windupTime) * windupTime
        if (raceDistance > race.distance) {
            debugPrintln("Beaten 2!")
            end = windupTime.toLong()
            break
        }
    }

    answer(end - start + 1)
}