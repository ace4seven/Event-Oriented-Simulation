package app.stats

private class RStatistics() {
    var averageWaitingForService: Double = 0.0
    var averageWaitingForMeal: Double = 0.0
    var averageWaitingForPay: Double = 0.0

    var leavePercentage: Double = 0.0
}

enum class AverageWaitingType {
    SERVICE, MEAL, PAY, ALL
}

class Statistics {
    private var rStatistics = RStatistics()

    private var cReplication: Int = 0

    private var averageWaitingForService: Double = 0.0
    private var averageWaitingForMeal: Double = 0.0
    private var averageWaitingForPay: Double = 0.0

    private var leavedCustomers: Int = 0
    private var customersSums: Int = 0

    fun getLeavedCustomersPercentage(): Double {
        return rStatistics.leavePercentage / cReplication
    }

    fun getAverageTimeCustomerWait(type: AverageWaitingType): Double {
        when (type) {
            AverageWaitingType.SERVICE -> return rStatistics.averageWaitingForService / cReplication.toDouble()
            AverageWaitingType.MEAL -> return rStatistics.averageWaitingForMeal / cReplication.toDouble()
            AverageWaitingType.PAY -> return rStatistics.averageWaitingForPay / cReplication.toDouble()
            AverageWaitingType.ALL -> {
                return (rStatistics.averageWaitingForPay + rStatistics.averageWaitingForMeal + rStatistics.averageWaitingForService) / cReplication
            }
        }
    }

    fun increaseAverage(entry: WaitingEntry, count: Int) {
        when(entry.type) {
            WaitType.FORSERVICE -> averageWaitingForService += (entry.result * count)
            WaitType.FORMEAL -> averageWaitingForMeal += (entry.result * count)
            WaitType.FORPAY -> averageWaitingForPay += (entry.result * count)
        }
    }

    fun incReplication() {
        cReplication += 1
    }

    fun customerIn(count: Int) {
        customersSums += count
    }

    fun leaveCustomer(count: Int) {
        leavedCustomers += count
    }

    fun updateWithReplication() {
        rStatistics.averageWaitingForService += (averageWaitingForService / (customersSums - leavedCustomers))
        rStatistics.averageWaitingForMeal += (averageWaitingForMeal / (customersSums - leavedCustomers))
        rStatistics.averageWaitingForPay += (averageWaitingForPay / (customersSums - leavedCustomers))

        rStatistics.leavePercentage += leavedCustomers.toDouble() / customersSums.toDouble()

        averageWaitingForService = 0.0
        averageWaitingForMeal = 0.0
        averageWaitingForPay = 0.0
        leavedCustomers = 0
        customersSums = 0
    }

}