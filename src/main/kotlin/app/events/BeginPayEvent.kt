package app.events

import app.model.CustomerGroup
import app.model.Waiter
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class BeginPayEvent(override val time: Double, val customerGroup: CustomerGroup, val waiter: Waiter): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        waiter.startWorking(rCore.cTime)

        rCore.planEvent(EndPayEvent(rCore.cTime + rCore.payGenerator.nextDouble(), waiter, customerGroup))

        C.message("Zaciatok platenia pre: ${customerGroup.type.desc()}")
    }

}