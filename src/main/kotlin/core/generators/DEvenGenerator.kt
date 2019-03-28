package core.generators

interface DEvenGeneratorDelegate {
    fun nextInt(): Int
}

class DEvenGenerator(val min: Int, val max: Int, seed: Long?): Generator(seed), DEvenGeneratorDelegate {

    override fun nextInt(): Int {
        return randomGenerator.nextInt(max - min) + min
    }

}