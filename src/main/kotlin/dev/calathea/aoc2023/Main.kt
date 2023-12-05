package dev.calathea.aoc2023

import dev.calathea.aoc2023.days.*
import kotlin.math.exp

fun main(args: Array<String>) {
    println("Hello World!")

    Day1("Day 1: Part 1") {
        expectAnswer(57346)
    }

    Day1Part2("Day 1: Part 2") {
        expectAnswer(57345)
    }


    Day2("Day 2: Part 1") {
        expectAnswer(2505)
    }

    Day2Part2("Day 2: Part 2") {
        expectAnswer(70265)
    }


    Day3("Day 3: Part 1") {
        // enableDebug = true
        expectAnswer(532445)
    }

    Day3Part2("Day 3: Part 2") {
        expectAnswer(79842967)
    }


    Day4("Day 4: Part 1") {
        expectAnswer(26443)
    }

    Day4Part2("Day 4: Part 2") {
        expectAnswer(6284877)
    }

    Day4Part2("Day 4: Part 2: Test") {
        // enableDebug = true
        expectAnswer(30)
        context = FileInput("day4_test.txt")
    }
}