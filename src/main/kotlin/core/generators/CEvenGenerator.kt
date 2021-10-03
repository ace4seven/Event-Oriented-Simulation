package core.generators

interface CEvenGeneratorDelegate {
    fun nextDouble(): Double
}

class CEvenGenerator(val min: Double = 0.0, val max: Double = 1.0, seed: Long?): Generator(seed), CEvenGeneratorDelegate {

    override fun nextDouble(): Double {
        return min + ((max - min) * randomGenerator.nextDouble())
    }
}