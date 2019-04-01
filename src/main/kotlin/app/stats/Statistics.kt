package app.stats

import core.ConfidenceInterval

private class RStatistics {
    var averageWaitingForService = ConfidenceInterval()
    var averageWaitingForMeal = ConfidenceInterval()
    var averageWaitingForPay = ConfidenceInterval()
    var averageWaitingTime = ConfidenceInterval()

    var averageBussinesTime = ConfidenceInterval()

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
    private var businessTime: Double = 0.0

    var averageWaitingForService: Double = 0.0
    var averageWaitingForMeal: Double = 0.0
    var averageWaitingForPay: Double = 0.0

    var waitingTime = 0.0
    var customersFinishEating = 0

    var averageFreeTimeChef = HashMap<Int, Double>()
    var averageFreeTimeWaiter = HashMap<Int, Double>()
    var freeWaitersWeight = WeightAveragesStat()
    var freeChefssWeight = WeightAveragesStat()
    var freeTableTwoWeight = WeightAveragesStat()
    var freeTableSixWeight = WeightAveragesStat()
    var freeTableFourWeight = WeightAveragesStat()
    var simulationTimeDuration = 0.0
    var leavedCustomers: Int = 0
    var customersSums: Int = 0

    fun getCurrentReplication(): Int {
        return cReplication
    }

    // MARK: - Replication statistics

    fun getWorkingTimes(): Pair<HashMap<Int, Double>, HashMap<Int, Double>> {
        return Pair(averageFreeTimeWaiter, averageFreeTimeChef)
    }

    fun getTimeCustomersWait(type: AverageWaitingType? = null): Double {
        if (type == null) {
            return (averageWaitingForMeal + averageWaitingForPay + averageWaitingForService) / if (customersSums - leavedCustomers == 0) 0 else customersSums - leavedCustomers
        }
        when(type) {
            AverageWaitingType.SERVICE -> return averageWaitingForService / if (customersSums - leavedCustomers == 0) 0 else customersSums - leavedCustomers
            AverageWaitingType.MEAL -> return averageWaitingForMeal / if (customersSums - leavedCustomers == 0) 0 else customersSums - leavedCustomers
            AverageWaitingType.PAY -> return averageWaitingForPay / if (customersSums - leavedCustomers == 0) 0 else customersSums - leavedCustomers
        }
    }


    // MARK: - SIMULATION STATISTICS

    fun getAverageWorkingTimes(): Triple<HashMap<Int, Double>, HashMap<Int, Double>, Int> {
        return Triple(rStatistics.averageFreeTimeWaiter, rStatistics.averageFeeTimeChef, cReplication)
    }

    fun getAverageArrivalStats(): Triple<Int, Int, ConfidenceInterval> {
        return Triple(rStatistics.customersSums / cReplication,
                rStatistics.leavedCustomers / cReplication,
                rStatistics.leavePercentage)
    }

    fun getAverageBusinessTime(): ConfidenceInterval {
        return rStatistics.averageBussinesTime
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

    fun getAverageWaitingTime(): ConfidenceInterval {
        return rStatistics.averageWaitingTime
    }

    fun getAverageHeight(type: HeightType): Triple<Double?, Double, Double?> {
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

    fun updateBusinessTime(time: Double) {
        this.businessTime = time
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
        rStatistics.averageWaitingForService.addMedianPart((averageWaitingForService / (customersSums - leavedCustomers).toDouble()))
        rStatistics.averageWaitingForMeal.addMedianPart((averageWaitingForMeal / (customersSums - leavedCustomers).toDouble()))
        rStatistics.averageWaitingForPay.addMedianPart((averageWaitingForPay / (customersSums - leavedCustomers).toDouble()))

        rStatistics.freeChefssWeight.addMedianPart(freeChefssWeight.getResult())
        rStatistics.freeWaitersWeight.addMedianPart(freeWaitersWeight.getResult())
        rStatistics.freeTableTwoWeight.addMedianPart(freeTableTwoWeight.getResult())
        rStatistics.freeTableFourWeight.addMedianPart(freeTableFourWeight.getResult())
        rStatistics.freeTableSixWeight.addMedianPart(freeTableSixWeight.getResult())

        rStatistics.leavedCustomers += leavedCustomers
        rStatistics.customersSums += customersSums

        rStatistics.averageWaitingTime.addMedianPart(waitingTime / customersFinishEating.toDouble())

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

        rStatistics.averageBussinesTime.addMedianPart(businessTime)
        clearNumbs()
    }

    private fun clearNumbs() {
        averageWaitingForService = 0.0
        averageWaitingForMeal = 0.0
        averageWaitingForPay = 0.0
        leavedCustomers = 0
        customersSums = 0
        customersFinishEating = 0
        waitingTime = 0.0

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

}