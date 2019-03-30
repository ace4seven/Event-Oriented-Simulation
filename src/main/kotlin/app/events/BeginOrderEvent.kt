package app.events

import app.model.CustomerGroup
import app.model.Waiter
import app.stats.WaitType
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class BeginOrderEvent(override val time: Double, val customerGroup: CustomerGroup, val waiter: Waiter): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        customerGroup.averageWaiting.stopTrack(rCore.cTime, WaitType.FORSERVICE)
        rCore.stats.increaseAverage(customerGroup.averageWaiting.getResult(WaitType.FORSERVICE), customerGroup.type.count())

        waiter.startWorking(rCore.cTime)

        rCore.planEvent(EndOrderEvent(rCore.cTime + rCore.serviceGenerator.nextDouble(), customerGroup, waiter))

        C.message("BEGIN ORDER: ${customerGroup.type.desc()}")
    }

}