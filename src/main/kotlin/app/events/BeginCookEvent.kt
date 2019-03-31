package app.events

import app.model.Chef
import app.model.Order
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class BeginCookEvent(override val time: Double, val meal: Order, val chef: Chef): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        chef.startWorking(time)
        rCore.planEvent(EndCookEvent(time + meal.orderSession.duration, chef, meal))
    }

    override fun debugPrint() {
        C.message("BEGIN COOKING(chef: ${chef.getID()}) " +
                "- Meal(name: ${meal.orderSession.foodType.foodName()}) " +
                "for customer(id: ${meal.customerGroup.getID()}, " +
                "table: ${meal.customerGroup.table().type.desc()})" +
                " TIME: ${time}")
    }

    override fun calendarDescription(): String {
        return "Začiatok varenia: ${meal.orderSession.foodType.foodName()}, Kuchár: ${chef.getID()} pre skupinu: ${meal.customerGroup.getID()}"
    }

}