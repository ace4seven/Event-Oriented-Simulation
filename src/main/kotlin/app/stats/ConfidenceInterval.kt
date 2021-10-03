package core
import kotlin.math.sqrt

class ConfidenceInterval {

    private var medPart: Double = 0.0
    private var numerator: Double = 0.0
    private var count: Int = 0

    private fun standDeviation(): Double {
        return sqrt((numerator / count) - Math.pow(medPart / count.toDouble(), 2.0))
    }

    fun median(): Double {
        return medPart / count.toDouble()
    }

    fun addMedianPart(part: Double) {
        medPart += part
        numerator += Math.pow(part, 2.0)
        count += 1
    }

    fun ISLeft(): Double? {
        if (count < 30) { return null }

        return median() - ((standDeviation() * 1.645) / sqrt(count.toDouble() - 1))
    }

    fun ISRight(): Double? {
        if (count < 30) { return null }

        return median() + ((standDeviation() * 1.645) / sqrt(count.toDouble() - 1))
    }

}