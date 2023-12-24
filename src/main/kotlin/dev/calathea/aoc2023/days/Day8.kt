package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.utils.lcm

private data class Node(val key: String, val left: String, val right: String)


val Day8 = Challenge(FileInput("day8.txt")) {
    val lines = input.split("\r\n")
    val rawDirections = lines.first()

    val nodes = mutableMapOf<String, Node>()

    for (line in lines.drop(2)) {
        val (key, children) = line.split(" = ")
        val (left, right) = children
            .removePrefix("(")
            .removeSuffix(")")
            .split(", ")

        nodes[key] = Node(key, left, right)
        debugPrintln(nodes[key])
    }

    val firstNode: Node = nodes["AAA"]!!
    val lastNode: Node = nodes["ZZZ"]!!

    var currentNode: Node = firstNode
    var total = 0

    a@ for (instruction in generateSequence { rawDirections.toList() }.flatten()) {
        when (instruction) {
            'R' -> currentNode = nodes[currentNode.right]!!
            'L' -> currentNode = nodes[currentNode.left]!!
        }
        total += 1
        if (currentNode.key == lastNode.key) break@a
    }
    answer(total)
}


val Day8Part2 = Challenge(FileInput("day8.txt")) {
    val lines = input.split("\r\n")
    val rawDirections = lines.first()

    val nodes = mutableMapOf<String, Node>()

    for (line in lines.drop(2)) {
        val (key, children) = line.split(" = ")
        val (left, right) = children
            .removePrefix("(")
            .removeSuffix(")")
            .split(", ")

        nodes[key] = Node(key, left, right)
    }

    val firstNodes: List<Node> = nodes.values.filter { it.key.endsWith('A') }



    val lcms = mutableListOf<Long>()
    for (node in firstNodes) {
        var distance = 0
        var activeNode = node
        a@for (item in generateSequence { rawDirections.toList() }.flatten()) {
            val newNode = when (item) {
                'L' -> nodes[activeNode.left]
                'R' -> nodes[activeNode.right]
                else -> null
            }!!

            if (activeNode.key.endsWith('Z')) {
                lcms += distance.toLong()
                break@a
            }
            distance += 1

            activeNode = newNode
        }
    }
    answer(lcms.lcm())
}