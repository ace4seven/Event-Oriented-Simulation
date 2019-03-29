package app.events

import app.model.CustomerGroup
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class EndEatingEvent(override val time: Double, val customerGroup: CustomerGroup): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        if (rCore.freeWaiters.size > 0) {
            rCore.planEvent(BeginPayEvent(rCore.cTime, customerGroup, rCore.freeWaiters.poll()))
        } else {
            rCore.fifoPayment.add(customerGroup)
        }

        C.message("Koniec jedenia pre: ${customerGroup.type.desc()}")
    }

}