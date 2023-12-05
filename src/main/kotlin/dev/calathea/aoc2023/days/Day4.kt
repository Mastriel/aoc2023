package dev.calathea.aoc2023.days

val Day4 = Challenge(FileInput("day4.txt")) {
    val lines = input.split("\r\n")

    var total = 0
    for ((lineIndex, line) in lines.withIndex()) {
        var gameTotal = 0
        val data = line.split(": ").last()
        val (winningNumbers, cardNumbers) = data.split(" | ")
        val winningNumbersList = winningNumbers.split(" ").filter { it.isNotEmpty() }
        val cardNumbersList = cardNumbers.split(" ").filter { it.isNotEmpty() }

        for (cardNumber in cardNumbersList) {
            if (winningNumbersList.contains(cardNumber)) {
                debugPrintln("L${lineIndex+1} matches")
                if (gameTotal == 0)
                    gameTotal = 1
                else gameTotal *= 2
            }
        }
        total += gameTotal
    }
    answer(total)
}


private data class Card(val index: Int, val winning: Int, var amount: Int)

val Day4Part2 = Challenge(FileInput("day4.txt")) {
    val lines = input.split("\r\n")

    val cards = mutableListOf<Card>()

    for ((lineIndex, line) in lines.withIndex()) {
        var winning = 0
        val data = line.split(": ").last()
        val (winningNumbers, cardNumbers) = data.split(" | ")
        val winningNumbersList = winningNumbers.split(" ").filter { it.isNotEmpty() }
        val cardNumbersList = cardNumbers.split(" ").filter { it.isNotEmpty() }

        for (cardNumber in cardNumbersList) {
            if (winningNumbersList.contains(cardNumber)) {
                winning += 1
            }
        }
        cards.add(Card(lineIndex, winning, 1))
    }

    debugPrintln(cards)

    val total = solveCards(cards)

    answer(total)
}

// I DON'T KNOW HOW THIS WORKS
private fun FileInput.solveCards(cards: List<Card>) : Int {
    var totalCards = 0
    val cardsList = cards.toMutableList()
    for ((cardIndex, card) in cards.withIndex()) {
        debugPrintln("Processing card ${cardIndex+1}")
        repeat(card.winning) { i ->
            val index = cardIndex + i + 1
            cardsList.getOrNull(index) ?: return@repeat

            cardsList[index].amount += card.amount

            debugPrintln(cardsList.map { "${it.index+1} winning ${it.winning} amount ${it.amount}" }[index])

            totalCards += card.amount
        }
    }
    return totalCards + cardsList.size
}