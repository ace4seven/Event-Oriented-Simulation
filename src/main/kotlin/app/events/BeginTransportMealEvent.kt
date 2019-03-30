package app.events

import app.model.CustomerGroup
import app.model.Waiter
import app.stats.WaitType
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class BeginTransportMealEvent(override val time: Double, val meal: CustomerGroup, val waiter: Waiter): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        waiter.startWorking(time)

        rCore.planEvent(EndTransportMealEvent(time + rCore.durationFoodToCustomerGenerator.nextDouble(), waiter, meal))
    }

    override fun debugPrint() {
        C.message("BEGIN TRANSPORT MEAL Customer(id: ${meal.getID()}, count: ${meal.type.count()}) Waiter(id: ${waiter.getID()}) TIME: $time")
    }

}