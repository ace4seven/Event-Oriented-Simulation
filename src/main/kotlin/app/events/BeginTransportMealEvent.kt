package app.events

import app.model.Waiter
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class BeginTransportMealEvent(override val time: Double, val waiter: Waiter): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        waiter.startWorking(rCore.cTime)

        val meal = rCore.fifoFinishMeal.pop()!!

        rCore.planEvent(EndTransportMealEvent(rCore.cTime + rCore.durationFoodToCustomerGenerator.nextDouble(), waiter, meal))

        C.message("Zaciatok odnesenia jedla pre: ${meal.type.desc()}")
    }

}