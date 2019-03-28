package app.events

import app.model.CustomerGroup
import app.model.Order
import app.model.Waiter
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class EndOrderEvent(override val time: Double, val customerGroup: CustomerGroup, val waiter: Waiter): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        for (i in 1..customerGroup.type.count()) {
            rCore.fifoOrder.add(Order(rCore.foodManager.prepareOrder(), customerGroup))
        }

        waiter.stopWorking(time)
        rCore.freeWaiters.add(waiter)

        if (rCore.fifoService.size() > 0) {
            rCore.planEvent(BeginOrderEvent(time, rCore.fifoService.pop()!!))
        } else if (rCore.fifoFinishMeal.size() > 0) {
            rCore.planEvent(BeginTransportMealEvent(time))
        } else if (rCore.fifoPayment.size() > 0) {
            rCore.planEvent(BeginPayEvent(time, rCore.fifoPayment.pop()!!))
        }

        if (rCore.freeChefs.size > 0) {
            rCore.planEvent(BeginCookEvent(time))
        }

        C.message("Ukončenie objednávania pre: ${customerGroup.type.desc()}")
    }

}