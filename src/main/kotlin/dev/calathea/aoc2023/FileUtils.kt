package dev.calathea.aoc2023

import dev.calathea.aoc2023.days.Challenge


fun readResource(path: String) : String {
    return Challenge::class.java.getResourceAsStream(path)?.readBytes()?.toString(Charsets.UTF_8)!!
}