package dev.calathea.aoc2023.days


private data class Conversion(val destinationRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long) {


}

private data class MappingKey(val source: String, val destination: String)


private fun List<Conversion>.sourceToDestination(source: Long) : Long {
    val list = this

    val conversion = list.find { source in it.sourceRangeStart..<it.sourceRangeStart+it.rangeLength } ?: return source

    val difference = conversion.sourceRangeStart - source

    return conversion.destinationRangeStart - difference

}



val Day5 = Challenge(FileInput("day5.txt")) {
    val lines = input.split("\r\n").toMutableList()

    val seedNumbers = lines.first().split(": ").last().split(" ").map { it.toLong() }

    val rawMaps = lines.drop(2).joinToString("\n").split("\n\n")

    // debugPrintln(rawMaps)

    val maps = mutableMapOf<MappingKey, MutableList<Conversion>>()

    for (rawMapping in rawMaps) {
        val (name) = rawMapping.split(" ", limit = 2)
        val (sourceKey, destinationKey) = name.split("-to-")
        val key = MappingKey(sourceKey, destinationKey)
        val mapData = rawMapping.split("\n").drop(1)
        for (data in mapData) {
            maps.putIfAbsent(key, mutableListOf())
            val (destination, source, rangeLength) = data.split(" ").map { it.toLong() }
            maps[key]!!.add(Conversion(destination, source, rangeLength))
        }
    }

    var lowestNumber = Long.MAX_VALUE
    for (seed in seedNumbers) {
        var number = seed
        for (map in maps) {
            number = map.value.sourceToDestination(number)
        }
        if (number < lowestNumber) lowestNumber = number
    }

    debugPrintln(maps)

    answer(lowestNumber)

}

val Day5Part2 = Challenge(FileInput("day5.txt")) {
    val lines = input.split("\r\n").toMutableList()

    val rawSeedNumbers = lines.first().split(": ").last().split(" ").map { it.toLong() }

    val seedNumbers = mutableListOf<Long>()



    val rawMaps = lines.drop(2).joinToString("\n").split("\n\n")

    // debugPrintln(rawMaps)

    val maps = mutableMapOf<MappingKey, MutableList<Conversion>>()

    for (rawMapping in rawMaps) {
        val (name) = rawMapping.split(" ", limit = 2)
        val (sourceKey, destinationKey) = name.split("-to-")
        val key = MappingKey(sourceKey, destinationKey)
        val mapData = rawMapping.split("\n").drop(1)
        for (data in mapData) {
            maps.putIfAbsent(key, mutableListOf())
            val (destination, source, rangeLength) = data.split(" ").map { it.toLong() }
            maps[key]!!.add(Conversion(destination, source, rangeLength))
        }
    }

    var lowestNumber = Long.MAX_VALUE

    val seedPairs = rawSeedNumbers.chunked(2)
    for ((index, pair) in seedPairs.withIndex()) {
        val (start, range) = pair
        repeat(range.toInt()) { i ->
            var number = start + i
            for (map in maps) {
                number = map.value.sourceToDestination(number)
            }
            if (number < lowestNumber) lowestNumber = number
        }
    }

    answer(lowestNumber)
}