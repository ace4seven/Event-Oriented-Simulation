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

        waiter.startWorking(rCore.cTime)

        rCore.planEvent(EndTransportMealEvent(rCore.cTime + rCore.durationFoodToCustomerGenerator.nextDouble(), waiter, meal))

        C.message("TRANSPORT MEAL: ${meal.type.desc()}")
    }

}