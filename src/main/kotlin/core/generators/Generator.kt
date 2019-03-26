import java.util.*
import kotlin.math.max

abstract class Generator(seed: Long?) {

    protected val randomGenerator: Random

    init {
        if (seed != null) {
            this.randomGenerator = Random(seed)
        } else {
            this.randomGenerator = Random()
        }
    }

}

class DiscreteGenerator(minValue: Int, maxValue: Int, seed: Long? = null): Generator(seed) {

    val min = minValue
    val max = maxValue

    fun generate(): Int {
        return randomGenerator.nextInt(max - min) + min
    }

}

class ContinuosGenerator(minValue: Double = 0.0, maxValue: Double = 1.0, seed: Long? = null): Generator(seed) {

    val min = minValue
    val max = maxValue

    fun generate(): Double {
        return min + ((max - min) * randomGenerator.nextDouble())
    }

}