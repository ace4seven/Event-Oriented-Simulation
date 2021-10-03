package app.events

import app.model.CustomerGroup
import app.model.Waiter
import app.stats.WaitType
import core.Event
import core.EventSimulationCore
import app.RestaurantSimulationCore

class EndTransportMealEvent(override val time: Double, val waiter: Waiter, val customerGroup: CustomerGroup): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore
        val canTrackWeights = !simulationCore.isCooling || simulationCore.cTime < simulationCore.maxTime

        if (customerGroup.averageWaiting.canStopTrack) {
            customerGroup.averageWaiting.stopTrack(time, WaitType.FORMEAL)
        }

        customerGroup.table().status = "Skupina ${customerGroup.getID()} - jedia"

        waiter.stopWorking(time)
        rCore.freeWaiters.add(waiter)
        if (canTrackWeights) {
            rCore.stats.freeWaitersWeight.addValue(time, rCore.freeWaiters.size - 1)
        }
        rCore.stats.averageFreeTimeWaiter[waiter.getID()] = waiter.getWorkingTime()

        var finishEatingTime = 0.0
        for (i in 1..customerGroup.type.count()) {
            val customerEatingTime = rCore.eatFoodGenerator.nextDouble()
            if (finishEatingTime < customerEatingTime) {
                finishEatingTime = customerEatingTime
            }
        }

        if (rCore.fifoService.size() > 0) {
            val group = rCore.fifoService.pop()!!
            if (canTrackWeights) {
                rCore.stats.freeWaitersWeight.addValue(time, rCore.freeWaiters.size)
            }
            rCore.planEvent(BeginOrderEvent(time, group, rCore.freeWaiters.poll()))
        } else if (rCore.fifoFinishMeal.size() > 0) {
            val meal = rCore.fifoFinishMeal.pop()!!
            if (canTrackWeights) {
                rCore.stats.freeWaitersWeight.addValue(time, rCore.freeWaiters.size)
            }
            rCore.planEvent(BeginTransportMealEvent(time, meal,  rCore.freeWaiters.poll()))
        } else if (rCore.fifoPayment.size() > 0) {
            val group = rCore.fifoPayment.pop()!!
            if (canTrackWeights) {
                rCore.stats.freeWaitersWeight.addValue(time, rCore.freeWaiters.size)
            }
            rCore.planEvent(BeginPayEvent(time, group, rCore.freeWaiters.poll()))
        }

        rCore.planEvent(EndEatingEvent(time + finishEatingTime, customerGroup))
    }

    override fun debugPrint() {
        C.message("BEGIN TRANSPORT MEAL Customer(id: ${customerGroup.getID()}, count: ${customerGroup.type.count()}) Waiter(id: ${waiter.getID()}) TIME: $time")
    }

    override fun calendarDescription(): String {
        return "Koniec transportu jedla pre ${customerGroup.getID()}, obsluha: ${waiter.getID()}"
    }

}