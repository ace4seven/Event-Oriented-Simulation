package app.events

import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class BeginTransportMealEvent(override val time: Double): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        val waiter = rCore.freeWaiters.poll()
        waiter.startWorking(rCore.cTime)

        val meal = rCore.fifoFinishMeal.pop()!!

        rCore.planEvent(EndTransportMealEvent(rCore.cTime + rCore.durationFoodToCustomerGenerator.nextDouble(), waiter, meal))

        C.message("Zaciatok odnesenia jedla pre: ${meal.type.desc()}")
    }

}