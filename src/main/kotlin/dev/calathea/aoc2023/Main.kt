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
        expectAnswer(532445)
    }

    Day3Part2("Day 3: Part 2") {
        expectAnswer(79842967)
    }


    Day4("Day 4: Part 1") {
        expectAnswer(26443)
    }

    Day4Part2("Day 4: Part 2: Test") {
        expectAnswer(30)
        context = FileInput("day4_test.txt")
    }

    Day4Part2("Day 4: Part 2") {
        expectAnswer(6284877)
    }


    Day5("Day 5: Part 1: Test") {
        context = FileInput("day5_test.txt")
        expectAnswer(35L)

    }

    Day5("Day 5: Part 1") {
        expectAnswer(226172555L)
    }


    Day5Part2("Day 5: Part 2: Test") {
        context = FileInput("day5_test.txt")
        expectAnswer(46L)
    }

    Day5Part2("Day 5: Part 2") {
        skip = true // entirely brute forced. took 8 minutes to run on an i7-10750H
        expectAnswer(47909639)
    }


    Day6("Day 6: Part 1: Test") {
        enableDebug = true
        context = FileInput("day6_test.txt")
        expectAnswer(288)
    }

    Day6("Day 6: Part 1") {
        expectAnswer(316800)
    }


    Day6Part2("Day 6: Part 2: Test") {
        context = FileInput("day6_test.txt")
        expectAnswer(71503L)
    }

    Day6Part2("Day 6: Part 2") {
        expectAnswer(45647654L)
    }

    Day7("Day 7: Part 1: Test") {
        context = FileInput("day7_test.txt")
        expectAnswer(6440)
    }

    Day7("Day 7: Part 1") {
        expectAnswer(253910319)
    }


    Day7Part2("Day 7: Part 2: Test") {
        context = FileInput("day7_test.txt")
        expectAnswer(5905)
    }

    Day7Part2("Day 7: Part 2") {
        expectAnswer(254083736)
    }

    Day8("Day 8: Part 1: Test") {
        context = FileInput("day8_test.txt")
        expectAnswer(6)
    }

    Day8("Day 8: Part 1") {
        expectAnswer(14257)
    }


    Day8Part2("Day 8: Part 2: Test") {
        enableDebug = true
        context = FileInput("day8_test2.txt")
        expectAnswer(6L)
    }

    Day8Part2("Day 8: Part 2") {
        expectAnswer(16187743689077L)
    }


}