package dev.calathea.aoc2023.days

import kotlin.time.TimeSource
import kotlin.time.measureTime


open class ChallengeException : Exception()

class ChallengeAnswerException(val answer: Any) : Exception()
class ChallengeFailureException(val reason: String, causedBy: Throwable?) : Exception(causedBy)


open class ChallengeContext(val name: String) {
    fun answer(answer: Any) : Nothing {
        throw ChallengeAnswerException(answer)
    }
    fun fail(reason: String, causedBy: Throwable? = null) : Nothing {
        throw ChallengeFailureException(reason, causedBy)
    }

    fun expect(condition: Boolean, message: () -> String = { "Assertation failed." }) : Nothing? {
        if (!condition) fail(message())
        return null
    }

    infix fun Any?.expectEquals(other: Any?) : Nothing? {
        expect(this == other) { "expected $this, got $other" }
        return null
    }
}

class FileInputChallengeContext(name: String, val input: String) : ChallengeContext(name)

class FileInputChallenge(val name: String, private val invoke: FileInputChallengeContext.() -> Unit) {
    fun execute(input: String) {
        val context = FileInputChallengeContext(name, input)
        val timer = TimeSource.Monotonic.markNow()
        try {
            invoke(context)
            val timePassed = timer.elapsedNow()
            println("Found no solution for '${name}' in $timePassed")
        } catch (e: ChallengeAnswerException) {
            val timePassed = timer.elapsedNow()
            println("Found solution for '${name}' in $timePassed: ${e.answer}")
        } catch (e: ChallengeFailureException) {
            val timePassed = timer.elapsedNow()
            println("Failed to find solution for '${name}' in $timePassed: ${e.reason}")
            e.cause?.printStackTrace()
        } catch (e: Exception) {
            val timePassed = timer.elapsedNow()
            println("Encountered an exception for '${name}' in $timePassed")
            e.printStackTrace()
        }

    }
}

class Challenge(val name: String, private val invoke: ChallengeContext.() -> Unit) {

    fun execute() {
        val context = ChallengeContext(name)
        val timer = TimeSource.Monotonic.markNow()
        try {
            invoke(context)
            val timePassed = timer.elapsedNow()
            println("Found no solution for '${name}' in $timePassed")
        } catch (e: ChallengeAnswerException) {
            val timePassed = timer.elapsedNow()
            println("Found solution for '${name}' in $timePassed: ${e.answer}")
        } catch (e: ChallengeFailureException) {
            val timePassed = timer.elapsedNow()
            println("Failed to find solution for '${name}' in $timePassed: ${e.reason}")
            e.cause?.printStackTrace()
        } catch (e: Exception) {
            val timePassed = timer.elapsedNow()
            println("Encountered an exception for '${name}' in $timePassed")
            e.printStackTrace()
        }

    }
}