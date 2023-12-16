package dev.calathea.aoc2023.days

fun hash(string: String) : Int {
    var value = 0
    for (char in string) {
        value += char.code
        value *= 17
        value %= 256
    }
    return value
}

val Day15 = Challenge(FileInput("day15.txt")) {
    val input = input.filter { it != '\n' && it != '\r' }.split(",")

    var total = 0
    for (line in input) {
        total += hash(line)
    }
    answer(total)
}

sealed class Instruction() {
    abstract val label: String

    data class Remove(override val label: String) : Instruction()

    data class Set(override val label: String, val focalLength: Int) : Instruction()
}

fun parseInstructions(input: List<String>) : List<Instruction> {
    val instructions = mutableListOf<Instruction>()
    for (raw in input) {
        if (raw.contains("=")) {
            val (label, amount) = raw.split("=")
            instructions += Instruction.Set(label, amount.toInt())
        }
        if (raw.contains("-")) {
            val (label) = raw.split("-")
            instructions += Instruction.Remove(label)
        }
    }
    return instructions
}

data class Lens(var label: String, var focalLength: Int)

class Box {
    val list = mutableListOf<Lens>()

    fun has(label: String) = list.any { it.label == label }
    fun remove(label: String) = list.removeIf { it.label == label }
    fun set(label: String, amount: Int) {
        val lens = list.firstOrNull { it.label == label }
        if (lens != null) {
            lens.focalLength = amount
        } else {
            list.add(Lens(label, amount))
        }
    }
    fun get(label: String) = list.firstOrNull { it.label == label }

    fun isEmpty() = list.isEmpty()
}

val Day15Part2 = Challenge(FileInput("day15.txt")) {
    val input = input.filter { it != '\n' && it != '\r' }.split(",")

    val instructions = parseInstructions(input)

    val boxes = MutableList(256) { Box() }

    for (instruction in instructions) {
        val box = boxes[hash(instruction.label)]

        when (instruction) {
            is Instruction.Remove -> {
                if (box.has(instruction.label)) {
                    box.remove(instruction.label)
                }
            }
            is Instruction.Set -> {
                box.set(instruction.label, instruction.focalLength)
            }
        }
    }

    for ((i, box) in boxes.withIndex()) {
        if (box.isEmpty()) continue
        debugPrintln("${i}. ${box.list}")
    }

    var total = 0
    for ((i, box) in boxes.withIndex()) {
        for ((i2, lens) in box.list.withIndex()) {
            val value = (i + 1) * (i2 + 1) * lens.focalLength
            debugPrintln(value)
            total += value
        }
    }

    answer(total)


}