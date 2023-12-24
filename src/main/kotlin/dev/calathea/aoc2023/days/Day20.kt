package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.utils.lcm


private enum class PulseState { Low, High }

private sealed class Module {

    abstract val label: String
    abstract val destinations: List<String>

    data class Broadcast(override val label: String, override val destinations: List<String>) : Module()

    data class FlipFlop(
        override val label: String,
        override val destinations: List<String>,
        var state: Boolean = false
    ) : Module()

    data class Conjunction(
        override val label: String,
        override val destinations: List<String>,
        var pulseStates: MutableMap<String, PulseState> = mutableMapOf()
    ) : Module()

    data class Unused(
        override val label: String,
        override val destinations: List<String> = listOf()
    ) : Module()
}

private fun FileInput.parseInput(): List<Module> {
    val lines = input.split("\r\n")
        .map { it.split(" -> ") }

    val modules = mutableListOf<Module>()

    fun parseDestinations(rawDestinations: String) = rawDestinations.split(", ")

    for ((label, destination) in lines) {
        if (label.startsWith("%")) {
            modules += Module.FlipFlop(label.drop(1), parseDestinations(destination))
        } else if (label.startsWith("&")) {
            modules += Module.Conjunction(label.drop(1), parseDestinations(destination))
        } else if (label.startsWith("broadcaster")) {
            modules += Module.Broadcast(label, parseDestinations(destination))
        }
    }

    for (module in modules) {
        if (module is Module.Conjunction) {
            val inputs = modules.filter { module.label in it.destinations }
            for (input in inputs) {
                module.pulseStates[input.label] = PulseState.Low
            }
        }
    }

    return modules
}

private fun List<Module>.getDestinations(module: Module) = module.destinations.map {
    this.find { mod -> mod.label == it }
}

private data class ConnectionState(val inputModule: Module, val pulseState: PulseState, val currentModule: Module)


val Day20 = Challenge(FileInput("day20.txt")) {
    val modules = parseInput()


    debugPrintln(modules)

    val broadcast = modules.first { it is Module.Broadcast }

    var totalLow = 0
    var totalHigh = 0

    fun MutableList<ConnectionState>.sendPulse(comingFrom: Module, pulseState: PulseState) {
        val destinations = modules.getDestinations(comingFrom)

        val newList = mutableListOf<ConnectionState>()
        for (destination in destinations) {
            debugPrintln("${comingFrom.label} -${pulseState}-> ${destination?.label}")
            debugPrintln("COMING FROM: ${comingFrom}")
            debugPrintln("GOING TO: ${destination}")
            debugPrintln("")
            if (pulseState == PulseState.Low) totalLow += 1
            if (pulseState == PulseState.High) totalHigh += 1

            if (destination == null) {
                continue
            }


            val connectionState = ConnectionState(comingFrom, pulseState, destination)

            newList += connectionState
        }
        this.addAll(0, newList)
    }

    repeat(1000) { iteration ->
        totalLow += 1 // button press
        val remaining = mutableListOf(
            ConnectionState(broadcast, PulseState.Low, broadcast)
        )
        // if (isDebug) Thread.sleep(1000)
        while (remaining.isNotEmpty()) {
            for (connection in remaining.toList()) {
                //debugPrintln("${connection.inputModule.label} going into ${connection.currentModule.label} with ${connection.pulseState}")
                when (val module = connection.currentModule) {
                    is Module.Broadcast -> {
                        remaining.sendPulse(module, PulseState.Low)
                    }

                    is Module.Conjunction -> {
                        module.pulseStates[connection.inputModule.label] = connection.pulseState

                        if (module.pulseStates.values.all { it == PulseState.High }) {
                            remaining.sendPulse(module, PulseState.Low)
                        } else {
                            remaining.sendPulse(module, PulseState.High)
                        }
                    }

                    is Module.FlipFlop -> {
                        if (connection.pulseState == PulseState.Low) {
                            module.state = !module.state
                            if (module.state) {
                                remaining.sendPulse(module, PulseState.High)
                            } else {
                                remaining.sendPulse(module, PulseState.Low)
                            }
                        }
                    }
                    else -> {}
                }
                remaining.remove(connection)
            }
        }
    }


    answer(totalLow * totalHigh)
}

val Day20Part2 = Challenge(FileInput("day20.txt")) {
    val modules = parseInput()


    debugPrintln(modules)

    var buttonPresses = 0
    val broadcast = modules.first { it is Module.Broadcast }

    fun MutableList<ConnectionState>.sendPulse(comingFrom: Module, pulseState: PulseState) {
        val destinations = modules.getDestinations(comingFrom)

        val newList = mutableListOf<ConnectionState>()
        for (destination in destinations) {
            debugPrintln("${comingFrom.label} -${pulseState}-> ${destination?.label}")
            debugPrintln("COMING FROM: ${comingFrom}")
            debugPrintln("GOING TO: ${destination}")
            debugPrintln("")

            if (destination == null) continue
            val connectionState = ConnectionState(comingFrom, pulseState, destination)

            newList += connectionState
        }
        this.addAll(0, newList)
    }

    val states = mutableMapOf<String, Int>()

    fun pressButton() : Boolean {
        buttonPresses += 1
        val remaining = mutableListOf(
            ConnectionState(broadcast, PulseState.Low, broadcast)
        )
        while (remaining.isNotEmpty()) {
            for (connection in remaining.toList()) {
                // TODO make more generic! this will totally only work on my input
                val list = "pv, qh, xm, hz".split(", ")

                if (connection.currentModule.label in list && connection.pulseState == PulseState.Low) {
                    if (states[connection.currentModule.label] == null) {
                        states[connection.currentModule.label] = buttonPresses
                        if (states.size == list.size) return false
                    }
                }
                //debugPrintln("${connection.inputModule.label} going into ${connection.currentModule.label} with ${connection.pulseState}")
                when (val module = connection.currentModule) {
                    is Module.Broadcast -> {
                        remaining.sendPulse(module, PulseState.Low)
                    }

                    is Module.Conjunction -> {
                        module.pulseStates[connection.inputModule.label] = connection.pulseState

                        if (module.pulseStates.values.all { it == PulseState.High }) {
                            remaining.sendPulse(module, PulseState.Low)
                        } else {
                            remaining.sendPulse(module, PulseState.High)
                        }
                    }

                    is Module.FlipFlop -> {
                        if (connection.pulseState == PulseState.Low) {
                            module.state = !module.state
                            if (module.state) {
                                remaining.sendPulse(module, PulseState.High)
                            } else {
                                remaining.sendPulse(module, PulseState.Low)
                            }
                        }
                    }
                    else -> {}
                }
                remaining.remove(connection)
            }
        }
        return true
    }

    while (true) {
        if (!pressButton()) {
            println(states.values)
            answer(states.values.map { it.toLong() }.lcm())
        }
    }
}
