class SimulationStats {

    private var customerCount: Int = 0
    private var waitingInQueue: Double = 0.0

    // MARK: - weighted average attributes
    private var lastTime: Double = 0.0
    private var numerator: Double = 0.0
    private var denominator: Double = 0.0

    // MARK: - PUBLIC STATISTICAL GETTERS
    fun averageWaitingTimeInQueue(): Double {
        return waitingInQueue / customerCount.toDouble()
    }

    fun queueHeight(): Double {
        return numerator / denominator
    }

    fun increaseWaitingInQueue(value: Double) {
        waitingInQueue += value
        customerCount += 1
    }

    fun changeQueue(atTime: Double, size: Int) {
        val weight = atTime - lastTime

        denominator += weight
        numerator += weight * size

        lastTime = atTime
    }

}