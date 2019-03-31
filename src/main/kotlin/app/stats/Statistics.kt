package app.stats

import core.ConfidenceInterval

private class RStatistics {
    var averageWaitingForService = ConfidenceInterval()
    var averageWaitingForMeal = ConfidenceInterval()
    var averageWaitingForPay = ConfidenceInterval()

    var freeWaitersWeight = ConfidenceInterval()
    var freeChefssWeight = ConfidenceInterval()
    var freeTableTwoWeight = ConfidenceInterval()
    var freeTableSixWeight = ConfidenceInterval()
    var freeTableFourWeight = ConfidenceInterval()

    var leavePercentage = ConfidenceInterval()
    var leavedCustomers: Int = 0
    var customersSums: Int = 0

    var averageFeeTimeChef = HashMap<Int, Double>()
    var averageFreeTimeWaiter = HashMap<Int, Double>()
}

enum class AverageWaitingType {
    SERVICE, MEAL, PAY
}

enum class HeightType {
    WAITER, CHEF, TABLE_TWO, TABLE_FOUR, TABLE_SIX
}

class Statistics() {
    private var rStatistics = RStatistics()
    private var cReplication: Int = 0
    private var averageWaitingForService: Double = 0.0
    private var averageWaitingForMeal: Double = 0.0
    private var averageWaitingForPay: Double = 0.0

    var averageFreeTimeChef = HashMap<Int, Double>()
    var averageFreeTimeWaiter = HashMap<Int, Double>()

    var freeWaitersWeight = WeightAveragesStat()
    var freeChefssWeight = WeightAveragesStat()
    var freeTableTwoWeight = WeightAveragesStat()
    var freeTableSixWeight = WeightAveragesStat()
    var freeTableFourWeight = WeightAveragesStat()

    private var leavedCustomers: Int = 0
    private var customersSums: Int = 0

    var simulationTimeDuration = 0.0

    fun getCurrentReplication(): Int {
        return cReplication
    }


    fun getAverageWorkingTimes(): Triple<HashMap<Int, Double>, HashMap<Int, Double>, Int> {
        return Triple(rStatistics.averageFreeTimeWaiter, rStatistics.averageFeeTimeChef, cReplication)
    }

    fun getArrivalStats(): Triple<Int, Int, ConfidenceInterval> {
        return Triple(rStatistics.customersSums / cReplication,
                rStatistics.leavedCustomers / cReplication,
                rStatistics.leavePercentage)
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

        rStatistics.freeChefssWeight.addMedianPart(freeChefssWeight.getWeight())
        rStatistics.freeWaitersWeight.addMedianPart(freeWaitersWeight.getWeight())
        rStatistics.freeTableTwoWeight.addMedianPart(freeTableTwoWeight.getWeight())
        rStatistics.freeTableFourWeight.addMedianPart(freeTableFourWeight.getWeight())
        rStatistics.freeTableSixWeight.addMedianPart(freeTableSixWeight.getWeight())

        rStatistics.leavedCustomers += leavedCustomers
        rStatistics.customersSums += customersSums

        averageFreeTimeWaiter.forEach {
            val prev = rStatistics.averageFreeTimeWaiter[it.key]
            if (prev != null) {
                rStatistics.averageFreeTimeWaiter[it.key] = prev + (simulationTimeDuration - it.value) / simulationTimeDuration
            } else { rStatistics.averageFreeTimeWaiter[it.key] = (simulationTimeDuration - it.value) / simulationTimeDuration }
        }

        averageFreeTimeChef.forEach {
            val prev = rStatistics.averageFeeTimeChef[it.key]
            if (prev != null) {
                rStatistics.averageFeeTimeChef[it.key] = prev + (simulationTimeDuration - it.value) / simulationTimeDuration
            } else { rStatistics.averageFeeTimeChef[it.key] = (simulationTimeDuration - it.value) / simulationTimeDuration }
        }

        rStatistics.leavePercentage.addMedianPart(leavedCustomers.toDouble() / customersSums.toDouble())

        clearNumbs()
    }

    private fun clearNumbs() {
        averageWaitingForService = 0.0
        averageWaitingForMeal = 0.0
        averageWaitingForPay = 0.0
        leavedCustomers = 0
        customersSums = 0

        averageFreeTimeWaiter.clear()
        averageFreeTimeChef.clear()

        freeWaitersWeight = WeightAveragesStat()
        freeChefssWeight = WeightAveragesStat()
        freeTableTwoWeight = WeightAveragesStat()
        freeTableSixWeight = WeightAveragesStat()
        freeTableFourWeight = WeightAveragesStat()
    }

    fun reset() {
        rStatistics = RStatistics()
        clearNumbs()
    }

    fun getHeight(type: HeightType): Triple<Double?, Double, Double?> {
        var isLeft: Double? = null
        var isRight: Double? = null

        when (type) {
            HeightType.WAITER -> {
                if (cReplication > 30) {
                    isLeft = rStatistics.freeWaitersWeight.ISLeft()!!
                    isRight = rStatistics.freeWaitersWeight.ISRight()!!
                }
                val median = rStatistics.freeWaitersWeight.median()

                return Triple(isLeft, median, isRight)
            }
            HeightType.CHEF -> {
                if (cReplication > 30) {
                    isLeft = rStatistics.freeChefssWeight.ISLeft()!!
                    isRight = rStatistics.freeChefssWeight.ISRight()!!
                }
                val median = rStatistics.freeChefssWeight.median()

                return Triple(isLeft, median, isRight)
            }
            HeightType.TABLE_TWO -> {
                if (cReplication > 30) {
                    isLeft = rStatistics.freeTableTwoWeight.ISLeft()!!
                    isRight = rStatistics.freeTableTwoWeight.ISRight()!!
                }
                val median = rStatistics.freeTableTwoWeight.median()

                return Triple(isLeft, median, isRight)
            }
            HeightType.TABLE_FOUR -> {
                if (cReplication > 30) {
                    isLeft = rStatistics.freeTableFourWeight.ISLeft()!!
                    isRight = rStatistics.freeTableFourWeight.ISRight()!!
                }
                val median = rStatistics.freeTableFourWeight.median()

                return Triple(isLeft, median, isRight)
            }
            HeightType.TABLE_SIX -> {
                if (cReplication > 30) {
                    isLeft = rStatistics.freeTableSixWeight.ISLeft()!!
                    isRight = rStatistics.freeTableSixWeight.ISRight()!!
                }
                val median = rStatistics.freeTableSixWeight.median()

                return Triple(isLeft, median, isRight)
            }
        }

    }

}