package app.events

import app.model.CustomerGroup
import app.model.Waiter
import app.stats.WaitType
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class BeginPayEvent(override val time: Double, val customerGroup: CustomerGroup, val waiter: Waiter): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        customerGroup.averageWaiting.stopTrack(rCore.cTime, WaitType.FORPAY)
        rCore.stats.increaseAverage(customerGroup.averageWaiting.getResult(WaitType.FORPAY), customerGroup.type.count())

        waiter.startWorking(rCore.cTime)

        rCore.planEvent(EndPayEvent(rCore.cTime + rCore.payGenerator.nextDouble(), waiter, customerGroup))

        C.message("BEGIN PAY: ${customerGroup.type.desc()}")
    }

}