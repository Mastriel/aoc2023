package dev.calathea.aoc2023.days

import dev.calathea.aoc2023.readResource
import kotlin.time.TimeSource


class ChallengeAnswerException(val answer: Any) : Exception()
class ChallengeFailureException(val reason: String, causedBy: Throwable?) : Exception(causedBy)


open class ChallengeContext {

    lateinit var challenge : Challenge<*>
        private set

    fun injectChallenge(challenge: Challenge<*>) { this.challenge = challenge }

    fun answer(answer: Any) : Nothing {
        throw ChallengeAnswerException(answer)
    }
    fun fail(reason: String, causedBy: Throwable? = null) : Nothing {
        throw ChallengeFailureException(reason, causedBy)
    }

    fun expect(condition: Boolean, message: () -> String = { "Assertion failed." }) : Nothing? {
        if (!condition) fail(message())
        return null
    }

    infix fun Any?.expectEquals(other: Any?) : Nothing? {
        expect(this == other) { "expected $other, got $this" }
        return null
    }

    fun debugPrintln(obj: Any?) {
        if (challenge.enableDebug) println(obj)
    }
}

class FileInput(fileName: String) : ChallengeContext() {
    val input = readResource("/$fileName")
}

class ChallengeModifier<T: ChallengeContext>(
    expectAnswer : Any? = null,
    var context : T? = null,
    var enableDebug : Boolean = false,
    var skip : Boolean = false
) {
    var expectAnswer = expectAnswer
        private set

    fun expectAnswer(value: Any?) { expectAnswer = value }
}


data class Challenge<T: ChallengeContext>(private var context: T, private val invoke: T.() -> Unit) {
    private var expectAnswer: Any? = null
    private var name: String? = null
    var enableDebug: Boolean = false
        private set

    var skip: Boolean = false
        private set

    /**
     * Configure and execute a challenge.
     */
    operator fun invoke(name: String, block: ChallengeModifier<T>.() -> Unit = {}) : Challenge<T> {
        val modifier = ChallengeModifier(
            expectAnswer,
            context,
            enableDebug,
            skip
        ).apply(block)

        return this.copy().apply {
            this.expectAnswer = modifier.expectAnswer
            this.name = name
            if (modifier.context != null) this.context = modifier.context!!
            this.enableDebug = modifier.enableDebug
            this.skip = modifier.skip
        }.also { it.execute() }
    }

    fun execute() {
        if (skip) {
            System.err.println("Skipping '${name}'...")
            return
        }
        val timer = TimeSource.Monotonic.markNow()
        val name = name ?: "Unnamed Challenge"
        try {
            context.injectChallenge(this)
            invoke(context)
            val timePassed = timer.elapsedNow()
            System.err.println("Found no solution for '${name}' in $timePassed")
        } catch (e: ChallengeAnswerException) {
            val timePassed = timer.elapsedNow()

            if (expectAnswer != null && expectAnswer != e.answer) {
                System.err.println("Failed to find solution for '${name}' in $timePassed: expected ${expectAnswer}, got ${e.answer}")
                return
            }
            println("Found solution for '${name}' in $timePassed: ${e.answer}")
        } catch (e: ChallengeFailureException) {
            val timePassed = timer.elapsedNow()
            System.err.println("Failed to find solution for '${name}' in $timePassed: ${e.reason}")
            e.cause?.printStackTrace()
        } catch (e: Exception) {
            val timePassed = timer.elapsedNow()
            System.err.println("Encountered an exception for '${name}' in $timePassed")
            e.printStackTrace()
        }

    }

}
