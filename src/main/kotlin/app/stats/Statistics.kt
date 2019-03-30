package app.stats

private class RStatistics() {
    var averageWaitingForService: Double = 0.0
    var averageWaitingForMeal: Double = 0.0
    var averageWaitingForPay: Double = 0.0

    var leavePercentage: Double = 0.0

    var averageWorkingTimesChefs = HashMap<Int, Double>()
    var averageWorkingTimesWaiters = HashMap<Int, Double>()

}

enum class AverageWaitingType {
    SERVICE, MEAL, PAY, ALL
}

class Statistics(val rTime: Double) {
    private var rStatistics = RStatistics()

    private var cReplication: Int = 0

    private var averageWaitingForService: Double = 0.0
    private var averageWaitingForMeal: Double = 0.0
    private var averageWaitingForPay: Double = 0.0

    var averageWorkingTimesChefs = HashMap<Int, Double>()
    var averageWorkingTimesWaiters = HashMap<Int, Double>()

    private var leavedCustomers: Int = 0
    private var customersSums: Int = 0

    fun getAverageWorkingTimes() {
        rStatistics.averageWorkingTimesWaiters.forEach {
            println("Waiter: ${it.key} : ${ ((it.value / cReplication)) * 100 }")
        }

        rStatistics.averageWorkingTimesChefs.forEach {
            println("Chef: ${it.key} : ${ ((it.value / cReplication)) * 100 }")
        }
    }

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

        averageWorkingTimesWaiters.forEach {
            val prev = rStatistics.averageWorkingTimesWaiters[it.key]
            if (prev != null) {
                rStatistics.averageWorkingTimesWaiters[it.key] = prev + (rTime - it.value) / rTime
            } else { rStatistics.averageWorkingTimesWaiters[it.key] = (rTime - it.value) / rTime }
        }

        averageWorkingTimesChefs.forEach {
            val prev = rStatistics.averageWorkingTimesChefs[it.key]
            if (prev != null) {
                rStatistics.averageWorkingTimesChefs[it.key] = prev + (rTime - it.value) / rTime
            } else { rStatistics.averageWorkingTimesChefs[it.key] = (rTime - it.value) / rTime }
        }

        rStatistics.leavePercentage += leavedCustomers.toDouble() / customersSums.toDouble()

        averageWaitingForService = 0.0
        averageWaitingForMeal = 0.0
        averageWaitingForPay = 0.0
        leavedCustomers = 0
        customersSums = 0

        averageWorkingTimesWaiters.clear()
        averageWorkingTimesChefs.clear()
    }

}