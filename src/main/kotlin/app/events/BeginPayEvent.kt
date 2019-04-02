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

        if (customerGroup.averageWaiting.canStopTrack) {
            customerGroup.averageWaiting.stopTrack(time, WaitType.FORPAY)
        }

        val endPayment = time + rCore.payGenerator.nextDouble()
        waiter.startWorking(time, endPayment)
        waiter.addStatus("Platenie skupina ${customerGroup.getID()}")

        customerGroup.table().setStatus("Skupina ${customerGroup.getID()} platí")

        rCore.planEvent(EndPayEvent(endPayment, waiter, customerGroup))
    }

    override fun debugPrint() {
        C.message("BEGIN PAY: Customer(id: ${customerGroup.getID()}, count: ${customerGroup.type.count()}) Waiter(id: ${waiter.getID()}) TIME: $time")
    }

    override fun calendarDescription(): String {
        return "Začiatok platenia: ${customerGroup.getID()}, obsluha: ${waiter.getID()}"
    }

}