package dev.calathea.aoc2023.days

private val ordering = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
private val orderingPart2 = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

private data class Hand(val cards: List<Char>, val bid: Int)

private data class OrderedHand(val hand: Hand, val primary: Int, val secondary: List<Int>)

private fun makeSecondaryOrdering(ordering: List<Char>, hand: Hand) : List<Int> {
    val list = mutableListOf<Int>()
    for (card in hand.cards) {
        list += ordering.indexOf(card)
    }
    return list
}

val Day7 = Challenge(FileInput("day7.txt")) {
    val lines = input.split("\r\n")
    val hands = lines.map {
        val item = it.split(" ")
        Hand(item[0].toList(), item[1].toInt())
    }

    val orderedHands = mutableListOf<OrderedHand>()
    for (hand in hands) {
        val types = mutableMapOf<Char, Int>()
        for (letter in hand.cards) {
            types[letter] = (types[letter] ?: 0) + 1
        }

        // full house
        if (types.size == 1) {
            orderedHands += OrderedHand(hand, 1, makeSecondaryOrdering(ordering, hand))
            continue
        }

        // four of a kind
        if (types.values.any { it == 4 }) {
            orderedHands += OrderedHand(hand, 2, makeSecondaryOrdering(ordering, hand))
            continue
        }

        // full house
        if (types.values.any { it == 3 } && types.values.any { it == 2 }) {
            orderedHands += OrderedHand(hand, 3, makeSecondaryOrdering(ordering, hand))
            continue
        }

        // three of a kind
        if (types.values.any { it == 3 }) {
            orderedHands += OrderedHand(hand, 4, makeSecondaryOrdering(ordering, hand))
            continue
        }

        // two pairs
        if (types.values.count { it == 2 } == 2) {
            orderedHands += OrderedHand(hand, 5, makeSecondaryOrdering(ordering, hand))
            continue
        }

        // 1 pair
        if (types.values.any { it == 2 }) {
            orderedHands += OrderedHand(hand, 6, makeSecondaryOrdering(ordering, hand))
            continue
        }

        orderedHands += OrderedHand(hand, 7, makeSecondaryOrdering(ordering, hand))
        // high card
    }

    val handsOrdered = orderedHands
        .asSequence()
        .sortedBy { it.secondary[4] }
        .sortedBy { it.secondary[3] }
        .sortedBy { it.secondary[2] }
        .sortedBy { it.secondary[1] }
        .sortedBy { it.secondary[0] }
        .sortedBy { it.primary }
        .toList()

    debugPrintln(handsOrdered.map { it.hand }.joinToString("\n"))

    var total = 0
    for ((i, hand) in handsOrdered.asReversed().withIndex()) {
        total += hand.hand.bid * (i + 1)
    }
    answer(total)
}

private fun getHand(handTypes: Map<Char, Int>, hand: Hand) : OrderedHand {
    // full house
    if (handTypes.size == 1) {
        return OrderedHand(hand, 1, makeSecondaryOrdering(orderingPart2, hand))
    }

    // four of a kind
    if (handTypes.values.any { it == 4 }) {
        return OrderedHand(hand, 2, makeSecondaryOrdering(orderingPart2, hand))
    }

    // full house
    if (handTypes.values.any { it == 3 } && handTypes.values.any { it == 2 }) {
        return OrderedHand(hand, 3, makeSecondaryOrdering(orderingPart2, hand))
    }

    // three of a kind
    if (handTypes.values.any { it == 3 }) {
        return OrderedHand(hand, 4, makeSecondaryOrdering(orderingPart2, hand))

    }

    // two pairs
    if (handTypes.values.count { it == 2 } == 2) {
        return OrderedHand(hand, 5, makeSecondaryOrdering(orderingPart2, hand))
    }

    // 1 pair
    if (handTypes.values.any { it == 2 }) {
        return OrderedHand(hand, 6, makeSecondaryOrdering(orderingPart2, hand))
    }

    // high card
    return OrderedHand(hand, 7, makeSecondaryOrdering(orderingPart2, hand))
}

val Day7Part2 = Challenge(FileInput("day7.txt")) {
    val lines = input.split("\r\n")
    val hands = lines.map {
        val item = it.split(" ")
        Hand(item[0].toList(), item[1].toInt())
    }

    val orderedHands = mutableListOf<OrderedHand>()
    for (hand in hands) {
        val types = mutableMapOf<Char, Int>()
        for (letter in hand.cards) {
            if (letter == 'J') {
                val testHands = mutableMapOf<Char, OrderedHand>()
                for (char in orderingPart2.filter { it in hand.cards }) {
                    val newHand = Hand(hand.cards.map { if (it == 'J') char else it }, hand.bid)
                    val testLetters = mutableMapOf<Char, Int>()
                    for (testLetter in newHand.cards) {
                        testLetters[testLetter] = (testLetters[testLetter] ?: 0) + 1
                    }
                    debugPrintln(testLetters)
                    testHands[char] = getHand(testLetters, newHand)
                }
                val bestLetter = testHands
                    .asSequence()
                    .sortedBy { it.value.secondary[4] }
                    .sortedBy { it.value.secondary[3] }
                    .sortedBy { it.value.secondary[2] }
                    .sortedBy { it.value.secondary[1] }
                    .sortedBy { it.value.secondary[0] }
                    .sortedBy { it.value.primary }
                    .toList()
                    .first()
                debugPrintln("J acting as ${bestLetter.key} in $hand (${bestLetter.value.hand})")
                types[bestLetter.key] = (types[bestLetter.key] ?: 0) + 1
                continue
            }
            types[letter] = (types[letter] ?: 0) + 1
        }

        orderedHands += getHand(types, hand)
    }

    val handsOrdered = orderedHands
        .asSequence()
        .sortedBy { it.secondary[4] }
        .sortedBy { it.secondary[3] }
        .sortedBy { it.secondary[2] }
        .sortedBy { it.secondary[1] }
        .sortedBy { it.secondary[0] }
        .sortedBy { it.primary }
        .toList()

    debugPrintln(handsOrdered.map { it.hand }.joinToString("\n"))

    var total = 0
    for ((i, hand) in handsOrdered.asReversed().withIndex()) {
        total += hand.hand.bid * (i + 1)
    }
    answer(total)
}