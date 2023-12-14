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
        context = FileInput("day8_test2.txt")
        expectAnswer(6L)
    }

    Day8Part2("Day 8: Part 2") {
        expectAnswer(16187743689077L)
    }


    Day9("Day 9: Part 1: Test") {
        context = FileInput("day9_test.txt")
        expectAnswer(114)
    }

    Day9("Day 9: Part 1") {
        expectAnswer(2005352194)
    }


    Day9Part2("Day 9: Part 2: Test") {
        expectAnswer(2)
        context = FileInput("day9_test.txt")
    }

    Day9Part2("Day 9: Part 2") {
        expectAnswer(1077)
    }


    Day10("Day 10: Part 1: Test") {
        context = FileInput("day10_test.txt")
        expectAnswer(8)
    }

    Day10("Day 10: Part 1") {
        skip = true
        expectAnswer(6831)
    }


    Day10Part2("Day 10: Part 2: Test") {
        expectAnswer(10)
        context = FileInput("day10_test2.txt")
    }

    Day10Part2("Day 10: Part 2: Test 2") {
        context = FileInput("day10_test3.txt")
        expectAnswer(4)
    }

    Day10Part2("Day 10: Part 2") {
        expectAnswer(305)
        enableDebug = true
        skip = true
    }


    Day11("Day 11: Part 1: Test") {
        context = FileInput("day11_test.txt")
    }

    Day11("Day 11: Part 1") {
        skip = true
        expectAnswer(9742154)
    }


    Day11Part2("Day 11: Part 2: Test") {
        context = FileInput("day11_test.txt")
    }

    Day11Part2("Day 11: Part 2") {
        skip = true
        expectAnswer(411142919886L)
    }

    Day12("Day 12: Part 1: Test") {
        context = FileInput("day12_test.txt")
    }

    Day12("Day 12: Part 1") {
        skip = true
        expectAnswer(7204)
    }


    Day12Part2("Day 12: Part 2: Test") {
        context = FileInput("day12_test.txt")
        enableDebug = true
    }

    Day12Part2("Day 12: Part 2") {
        skip = true
        expectAnswer(1672318386674L)
    }


    Day13("Day 13: Part 1: Test") {
        context = FileInput("day13_test.txt")
        expectAnswer(405)
    }

    Day13("Day 13: Part 1") {
        expectAnswer(30802)
    }


    Day13Part2("Day 13: Part 2: Test") {
        context = FileInput("day13_test.txt")
        expectAnswer(400)
    }

    Day13Part2("Day 13: Part 2") {
        expectAnswer(37876)
        skip = true
    }


    Day14("Day 14: Part 1: Test") {
        context = FileInput("day14_test.txt")
        enableDebug = true
        expectAnswer(136)
    }

    Day14("Day 14: Part 1") {
        expectAnswer(113456)
        skip = true
    }


    Day14Part2("Day 14: Part 2: Test") {
        context = FileInput("day14_test.txt")
        enableDebug = true
        expectAnswer(64)

    }

    Day14Part2("Day 14: Part 2") {
        enableDebug = true
        expectAnswer(118747)
        skip = true
    }
}