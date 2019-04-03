package app.events

import app.model.CustomerGroup
import app.model.Waiter
import core.Event
import core.EventSimulationCore
import app.RestaurantSimulationCore

class BeginTransportMealEvent(override val time: Double, val meal: CustomerGroup, val waiter: Waiter): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        val endTransportTime = time + rCore.durationFoodToCustomerGenerator.nextDouble()
        waiter.startWorking(time, endTransportTime)

        rCore.planEvent(EndTransportMealEvent(endTransportTime, waiter, meal))
    }

    override fun debugPrint() {
        C.message("BEGIN TRANSPORT MEAL Customer(id: ${meal.getID()}, count: ${meal.type.count()}) Waiter(id: ${waiter.getID()}) TIME: $time")
    }

    override fun calendarDescription(): String {
        return "Začiatok odnesenia jedla pre ${meal.getID()}, obsluha: ${waiter.getID()}"
    }

}