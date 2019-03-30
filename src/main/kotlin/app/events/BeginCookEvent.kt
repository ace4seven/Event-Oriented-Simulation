package app.events

import app.model.Chef
import app.model.Order
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class BeginCookEvent(override val time: Double, val meal: Order, val chef: Chef): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        chef.startWorking(simulationCore.cTime)

        rCore.planEvent(EndCookEvent(simulationCore.cTime + meal.orderSession.duration, chef, meal))

        C.message("BEGIN COOKING: ${meal.orderSession.foodType.foodName()} pre ${meal.customerGroup.type.desc()}")
    }

}