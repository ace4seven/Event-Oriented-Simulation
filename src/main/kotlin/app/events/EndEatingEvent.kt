package app.events

import app.model.CustomerGroup
import app.stats.WaitType
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class EndEatingEvent(override val time: Double, val customerGroup: CustomerGroup): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        customerGroup.table().setStatus("Skupina ${customerGroup.getID()} čaka na platenie")

        if (rCore.freeWaiters.size > 0) {
            rCore.planEvent(BeginPayEvent(time, customerGroup, rCore.freeWaiters.poll()))
        } else {
            customerGroup.averageWaiting.startTrack(time, WaitType.FORPAY)
            rCore.fifoPayment.add(customerGroup)
        }
    }

    override fun debugPrint() {
        C.message("END EAT: Customer(id: ${customerGroup.getID()}, count: ${customerGroup.type.count()})")
    }

    override fun calendarDescription(): String {
        return "Koniec jedenia, skupina: ${customerGroup.getID()}"
    }

}