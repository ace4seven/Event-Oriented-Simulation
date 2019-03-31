package app.events

import app.model.CustomerGroup
import app.model.Waiter
import app.stats.WaitType
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class EndTransportMealEvent(override val time: Double, val waiter: Waiter, val customerGroup: CustomerGroup): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        if (customerGroup.averageWaiting.canStopTrack) {
            customerGroup.averageWaiting.stopTrack(time, WaitType.FORMEAL)
            rCore.stats.increaseAverage(customerGroup.averageWaiting.getResult(WaitType.FORMEAL), customerGroup.type.count())
        }

        waiter.stopWorking(time)
        rCore.freeWaiters.add(waiter)
        rCore.stats.freeWaitersWeight.updateChange(time, rCore.freeWaiters.size)
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
            rCore.planEvent(BeginOrderEvent(time, group, rCore.freeWaiters.poll()))
            rCore.stats.freeWaitersWeight.updateChange(time, rCore.freeWaiters.size)
        } else if (rCore.fifoFinishMeal.size() > 0) {
            val meal = rCore.fifoFinishMeal.pop()!!
            rCore.planEvent(BeginTransportMealEvent(time, meal,  rCore.freeWaiters.poll()))
            rCore.stats.freeWaitersWeight.updateChange(time, rCore.freeWaiters.size)
        } else if (rCore.fifoPayment.size() > 0) {
            val group = rCore.fifoPayment.pop()!!
            rCore.planEvent(BeginPayEvent(time, group, rCore.freeWaiters.poll()))
            rCore.stats.freeWaitersWeight.updateChange(time, rCore.freeWaiters.size)
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