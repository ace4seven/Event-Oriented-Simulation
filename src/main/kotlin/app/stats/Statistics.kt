package app.stats

import core.ConfidenceInterval

private class RStatistics {
    var averageWaitingForService = ConfidenceInterval()
    var averageWaitingForMeal = ConfidenceInterval()
    var averageWaitingForPay = ConfidenceInterval()

    var leavePercentage: Double = 0.0

    var averageWorkingTimesChefs = HashMap<Int, Double>()
    var averageWorkingTimesWaiters = HashMap<Int, Double>()

}

enum class AverageWaitingType {
    SERVICE, MEAL, PAY
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

    fun getAverageTimeCustomerWait(type: AverageWaitingType? = null): Triple<Double?, Double, Double?> {
        if (type == null) {
            var isLeft: Double? = null
            var isRight: Double? = null

            if (cReplication > 30) {
                isLeft = rStatistics.averageWaitingForService.ISLeft()!! + rStatistics.averageWaitingForMeal.ISLeft()!! + rStatistics.averageWaitingForPay.ISLeft()!!
                isRight = rStatistics.averageWaitingForService.ISRight()!! + rStatistics.averageWaitingForMeal.ISRight()!! + rStatistics.averageWaitingForPay.ISRight()!!

            }
            val median = rStatistics.averageWaitingForMeal.median() + rStatistics.averageWaitingForPay.median() + rStatistics.averageWaitingForService.median()

            return Triple(isLeft, median, isRight)
        }

        when (type) {
            AverageWaitingType.SERVICE -> {
                return Triple(
                        rStatistics.averageWaitingForService.ISLeft(),
                        rStatistics.averageWaitingForService.median(),
                        rStatistics.averageWaitingForService.ISRight())
            }
            AverageWaitingType.MEAL -> {
                return Triple(
                        rStatistics.averageWaitingForMeal.ISLeft(),
                        rStatistics.averageWaitingForMeal.median(),
                        rStatistics.averageWaitingForMeal.ISRight())
            }
            AverageWaitingType.PAY -> {
                return Triple(
                        rStatistics.averageWaitingForPay.ISLeft(),
                        rStatistics.averageWaitingForPay.median(),
                        rStatistics.averageWaitingForPay.ISRight())
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
        rStatistics.averageWaitingForService.addMedianPart((averageWaitingForService / (customersSums - leavedCustomers)))
        rStatistics.averageWaitingForMeal.addMedianPart((averageWaitingForMeal / (customersSums - leavedCustomers)))
        rStatistics.averageWaitingForPay.addMedianPart((averageWaitingForPay / (customersSums - leavedCustomers)))

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