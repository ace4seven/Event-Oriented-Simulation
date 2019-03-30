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
        val rCore = simulationCore as RestaurantSimulationCore

        customerGroup.averageWaiting.startTrack(rCore.cTime, WaitType.FORMEAL)

        for (i in 1..customerGroup.type.count()) {
            rCore.fifoOrder.add(Order(rCore.foodManager.prepareOrder(), customerGroup))
        }

        waiter.stopWorking(rCore.cTime)
        rCore.freeWaiters.add(waiter)

        if (rCore.fifoService.size() > 0) {
            val group = rCore.fifoService.pop()!!
            rCore.planEvent(BeginOrderEvent(time, group, rCore.freeWaiters.poll()))
        } else if (rCore.fifoFinishMeal.size() > 0) {
            val meal = rCore.fifoFinishMeal.pop()!!
            rCore.planEvent(BeginTransportMealEvent(time, meal,  rCore.freeWaiters.poll()))
        } else if (rCore.fifoPayment.size() > 0) {
            val group = rCore.fifoPayment.pop()!!
            rCore.planEvent(BeginPayEvent(time, group, rCore.freeWaiters.poll()))
        }

        if (rCore.freeChefs.size > 0) {
            val cookEventsSum = min(rCore.freeChefs.size, rCore.fifoOrder.size())
            for (i in 1..cookEventsSum) {
                rCore.planEvent(BeginCookEvent(time, rCore.fifoOrder.pop()!!, rCore.freeChefs.poll()))
            }
        }

        C.message("Ukončenie objednávania pre: ${customerGroup.type.desc()}")
    }

}