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

        if (customerGroup.averageWaiting.canStopTrack) {
            customerGroup.averageWaiting.stopTrack(time, WaitType.FORSERVICE)
            rCore.stats.increaseAverage(customerGroup.averageWaiting.getResult(WaitType.FORSERVICE), customerGroup.type.count())
        }

        val endOrderTime = time + rCore.serviceGenerator.nextDouble()
        waiter.startWorking(time, endOrderTime)
        waiter.addStatus("Objednava pri ${customerGroup.getID()}")

        customerGroup.table().setStatus("Skupina ${customerGroup.getID()} objednáva")

        rCore.planEvent(EndOrderEvent(endOrderTime, customerGroup, waiter))
    }

    override fun debugPrint() {
        C.message("BEGIN ORDER Customer(id: ${customerGroup.getID()}, count: ${customerGroup.type.count()}) Waiter(id: ${waiter.getID()}) TIME: $time")
    }

    override fun calendarDescription(): String {
        return "Začiatok objednávky pre skupinu: ${customerGroup.getID()} čašníkom ${waiter.getID()}"
    }

}