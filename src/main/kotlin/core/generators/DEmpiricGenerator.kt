package core.generators.DEmpiricGenerator
import core.generators.DiscreteGenerator
import java.lang.Exception
import java.util.*

interface DEmpiricGeneratorDelegate {
    fun nextInt(): Int
}

open class DEmppiricDataType (
        val probability: Double, val generator: DiscreteGenerator
)

class DEmpiricGenerator(val dataSet: LinkedList<DEmppiricDataType>, val pGenerator: Random): DEmpiricGeneratorDelegate {

    override fun nextInt(): Int {
        val pRand = pGenerator.nextDouble()
        var pTemp: Double = 0.0

        dataSet.forEach {
            pTemp += it.probability
            if (pRand < pTemp) {
                return it.generator.generate()
            }
        }

        return -1
    }

}