package app.events

import app.model.CustomerGroup
import app.model.Order
import app.model.Waiter
import app.stats.WaitType
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore
import kotlin.math.min

class EndOrderEvent(override val time: Double, val customerGroup: CustomerGroup, val waiter: Waiter): Event() {

    override fun execute(simulationCore: EventSimulationCore) {

        customerGroup.table().setStatus("Skupina ${customerGroup.getID()} čaka na jedlo")

        val rCore = simulationCore as RestaurantSimulationCore

        for (i in 1..customerGroup.type.count()) {
            rCore.fifoOrder.add(Order(rCore.foodManager.prepareOrder(), customerGroup))
        }

        waiter.stopWorking(time)

        rCore.freeWaiters.add(waiter)
        rCore.stats.averageFreeTimeWaiter[waiter.getID()] = waiter.getWorkingTime()
        rCore.stats.freeWaitersWeight.updateChange(time, rCore.freeWaiters.size)

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

        customerGroup.averageWaiting.startTrack(time, WaitType.FORMEAL)
        if (rCore.freeChefs.size > 0) {
            val cookEventsSum = min(rCore.freeChefs.size, rCore.fifoOrder.size())
            for (i in 1..cookEventsSum) {
                rCore.planEvent(BeginCookEvent(time, rCore.fifoOrder.pop()!!, rCore.freeChefs.poll()))
            }
            rCore.stats.freeChefssWeight.updateChange(time, rCore.freeChefs.size)
        }
    }

    override fun debugPrint() {
        C.message("END ORDER Customer(id: ${customerGroup.getID()}, count: ${customerGroup.type.count()}) Waiter(id: ${waiter.getID()}) TIME: $time")
    }

    override fun calendarDescription(): String {
        return "Koniec objednavky pre skupinu ${customerGroup.getID()}, obsluha: ${waiter.getID()}"
    }

}