package app.events

import app.model.CustomerGroup
import app.model.Waiter
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class EndPayEvent(override val time: Double, val waiter: Waiter, val customerGroup: CustomerGroup): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        waiter.stopWorking(rCore.cTime)
        rCore.freeWaiters.add(waiter)

        rCore.tableManager.freeTable(customerGroup.table())

        if (rCore.fifoService.size() > 0) {
            rCore.planEvent(BeginOrderEvent(time, rCore.fifoService.pop()!!))
        } else if (rCore.fifoFinishMeal.size() > 0) {
            rCore.planEvent(BeginTransportMealEvent(time))
        } else if (rCore.fifoPayment.size() > 0) {
            rCore.planEvent(BeginPayEvent(time, rCore.fifoPayment.pop()!!))
        }

        C.message("Koniec platenia pre: ${customerGroup.type.desc()}")
    }

}