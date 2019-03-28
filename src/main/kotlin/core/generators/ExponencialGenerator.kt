package core.generators

import java.lang.Exception

interface ExponencialGeneratorDelegate {
    fun nextDouble(): Double
}

class ExponencialGenerator(val lambda: Double, seed: Long?): Generator(seed), ExponencialGeneratorDelegate {

    override fun nextDouble(): Double {
        val n = randomGenerator.nextDouble()
        if (n == 0.0) {
            throw Exception("Zero cannot be assign")
        }
        return -1 / lambda * Math.log(n)
    }

}