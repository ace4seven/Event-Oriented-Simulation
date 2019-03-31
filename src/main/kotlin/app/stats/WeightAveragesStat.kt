package app.stats

class WeightAveragesStat {

    private var lastTime: Double = 0.0
    private var numerator: Double = 0.0
    private var denominator: Double = 0.0

    fun updateChange(atTime: Double, size: Int) {
        val weight = atTime - lastTime

        denominator += weight
        numerator += weight * size

        lastTime = atTime
    }

    fun getWeight(): Double {
        return numerator / denominator
    }

}