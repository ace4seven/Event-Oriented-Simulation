package app.events

import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class BeginCookEvent(override val time: Double): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        val chef = rCore.freeChefs.poll()
        chef.startWorking(simulationCore.cTime)

        val meal = rCore.fifoOrder.pop()!!

        rCore.stats.count += 1
        rCore.stats.time += meal.orderSession.duration
        meal.customerGroup.incMeals()
        rCore.planEvent(EndCookEvent(simulationCore.cTime + meal.orderSession.duration, chef, meal))

        C.message("Zaciatok varenia: ${meal.orderSession.foodType.foodName()} pre ${meal.customerGroup.type.desc()}")
    }

}