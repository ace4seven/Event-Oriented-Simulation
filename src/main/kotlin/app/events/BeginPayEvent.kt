package app.events

import app.model.CustomerGroup
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class BeginPayEvent(override val time: Double, val customerGroup: CustomerGroup): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        val waiter = rCore.freeWaiters.poll()!!
        waiter.startWorking(rCore.cTime)

        rCore.planEvent(EndPayEvent(rCore.cTime + rCore.payGenerator.nextDouble(), waiter, customerGroup))

        C.message("Zaciatok platenia pre: ${customerGroup.type.desc()}")
    }

}