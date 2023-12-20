package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.utils.combinedWith
import dev.calathea.aoc2023.utils.size

sealed interface Output {
    data class OtherWorkflow(val workflowLabel: String) : Output
    data object Accept : Output
    data object Reject : Output
}

sealed class Rule {

    abstract val output: Output

    sealed class Comparative : Rule() {
        abstract val criteria: Criteria
        abstract val value: Int
        abstract fun truthyRange(): IntRange
        abstract fun falsyRange(): IntRange

    }

    data class MoreThan(override val criteria: Criteria, override val value: Int, override val output: Output) :
        Comparative() {
        override fun truthyRange(): IntRange = ((value + 1)..4000)
        override fun falsyRange(): IntRange = (1..value)
    }

    data class LessThan(override val criteria: Criteria, override val value: Int, override val output: Output) :
        Comparative() {
        override fun truthyRange(): IntRange = (1..<value)
        override fun falsyRange(): IntRange = (value..4000)
    }

    data class GoTo(override val output: Output) : Rule()
}

enum class Criteria { X, M, A, S }
data class Thing(val criterion: MutableMap<Criteria, Int>) {
    val totalXmas: Int
        get() =
            criterion.values.reduce { acc, it -> acc + it }
}

data class QuantumThing(
    private var x: IntRange,
    private var m: IntRange,
    private var a: IntRange,
    private var s: IntRange
) {
    operator fun get(criteria: Criteria): IntRange {
        return when (criteria) {
            Criteria.X -> x
            Criteria.M -> m
            Criteria.A -> a
            Criteria.S -> s
        }
    }

    operator fun set(criteria: Criteria, value: IntRange) {
        when (criteria) {
            Criteria.X -> x = value
            Criteria.M -> m = value
            Criteria.A -> a = value
            Criteria.S -> s = value
        }
    }

    fun size(): Long = x.size().toLong() * m.size().toLong() * a.size().toLong() * s.size().toLong()
}

private data class Workflow(val label: String, val rules: List<Rule>)


private fun createWorkflows(rawWorkflows: List<String>): List<Workflow> {
    val workflows = mutableListOf<Workflow>()

    for (line in rawWorkflows) {
        val label = line.split("{").first()
        val rawRules = line.split("{").last().removeSuffix("}").split(",")

        val rules = mutableListOf<Rule>()

        for (rawRule in rawRules) {
            if (!rawRule.contains(":")) {
                rules += Rule.GoTo(outputFromString(rawRule))
                continue
            } else {
                val (condition, rawOutput) = rawRule.split(":")
                val output = outputFromString(rawOutput)

                val criteria = Criteria.valueOf(condition[0].uppercase())

                val signConstructor = if (condition[1] == '<') Rule::LessThan else Rule::MoreThan

                val value = condition.drop(2).toInt()

                rules += signConstructor(criteria, value, output)
            }
        }

        workflows += Workflow(label, rules)
    }
    return workflows
}

private fun createThings(rawThings: List<String>): List<Thing> {
    val things = mutableListOf<Thing>()

    for (rawThing in rawThings) {
        val thing = Thing(mutableMapOf())
        rawThing.removePrefix("{")
            .removeSuffix("}")
            .split(",")
            .forEach {
                val criteria = Criteria.valueOf(it[0].uppercase())
                val amount = it.drop(2).toInt()
                thing.criterion[criteria] = amount
            }
        things += thing
    }
    return things
}

val Day19 = Challenge(FileInput("day19.txt")) {
    val (rawWorkflows, rawThings) = input.split("\r\n\r\n").map { it.split("\r\n") }

    val workflows = createWorkflows(rawWorkflows)
    val things = createThings(rawThings)

    var total = 0

    thing@ for (thing in things) {
        var currentWorkflow = workflows.first { it.label == "in" }

        g@ while (true) {
            for (rule in currentWorkflow.rules) {
                when (rule) {
                    is Rule.GoTo -> {
                        when (val output = rule.output) {
                            is Output.Accept -> {
                                total += thing.totalXmas
                                continue@thing
                            }

                            is Output.OtherWorkflow -> {
                                currentWorkflow = workflows.first { it.label == output.workflowLabel }
                                break
                            }

                            is Output.Reject -> continue@thing
                        }
                    }

                    is Rule.LessThan -> {
                        val amount = thing.criterion[rule.criteria]!!
                        if (amount < rule.value) {
                            when (val output = rule.output) {
                                is Output.Accept -> {
                                    total += thing.totalXmas
                                    continue@thing
                                }

                                is Output.OtherWorkflow -> {
                                    currentWorkflow = workflows.first { it.label == output.workflowLabel }
                                    break
                                }

                                is Output.Reject -> continue@thing
                            }
                        }
                    }

                    is Rule.MoreThan -> {
                        val amount = thing.criterion[rule.criteria]!!
                        if (amount > rule.value) {
                            when (val output = rule.output) {
                                is Output.Accept -> {
                                    total += thing.totalXmas
                                    continue@thing
                                }

                                is Output.OtherWorkflow -> {
                                    currentWorkflow = workflows.first { it.label == output.workflowLabel }
                                    break
                                }

                                is Output.Reject -> continue@thing
                            }
                        }
                    }
                }
            }
        }
    }

    answer(total)
}

private fun outputFromString(str: String): Output {
    if (str == "R") return Output.Reject
    if (str == "A") return Output.Accept
    return Output.OtherWorkflow(str)
}

val Day19Part2 = Challenge(FileInput("day19.txt")) {
    val (rawWorkflows) = input.split("\r\n\r\n").map { it.split("\r\n") }

    val workflows = createWorkflows(rawWorkflows)


    val quantumThing = QuantumThing(
        (1..4000),
        (1..4000),
        (1..4000),
        (1..4000)
    )

    val acceptedThings = recurse(Output.OtherWorkflow("in"), workflows, quantumThing)

    answer(acceptedThings)
}


private fun FileInput.recurse(prevOutput: Output, workflowsList: List<Workflow>, quantumThing: QuantumThing): Long {
    return when (prevOutput) {
        Output.Reject -> 0L
        Output.Accept -> quantumThing.size()
        is Output.OtherWorkflow -> {
            val workflow = workflowsList.first { it.label == prevOutput.workflowLabel }
            val newThing = quantumThing.copy()

            workflow.rules.sumOf { rule ->
                when (rule) {
                    is Rule.GoTo -> recurse(rule.output, workflowsList, newThing)
                    is Rule.Comparative -> {
                        val truthyRange = newThing[rule.criteria] combinedWith rule.truthyRange()
                        val falsyRange = newThing[rule.criteria] combinedWith rule.falsyRange()

                        newThing[rule.criteria] = truthyRange

                        recurse(rule.output, workflowsList, newThing).also { newThing[rule.criteria] = falsyRange }
                    }
                }
            }
        }
    }
}