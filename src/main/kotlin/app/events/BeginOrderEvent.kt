package app.events

import app.model.CustomerGroup
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class BeginOrderEvent(override val time: Double, val customerGroup: CustomerGroup): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        val waiter = rCore.freeWaiters.poll()
        waiter.startWorking(rCore.cTime)

        rCore.planEvent(EndOrderEvent(rCore.cTime + rCore.serviceGenerator.nextDouble(), customerGroup, waiter))

        C.message("Zaciatok objednávky pre: ${customerGroup.type.desc()}")
    }

}