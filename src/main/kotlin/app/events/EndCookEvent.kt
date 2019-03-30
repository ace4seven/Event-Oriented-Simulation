package app.events

import app.model.Chef
import app.model.Order
import app.stats.WaitType
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class EndCookEvent(override val time: Double, val chef: Chef, val order: Order): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        chef.stopWorking(rCore.cTime)
        rCore.freeChefs.add(chef)

        order.customerGroup.incMeals()

        if (order.customerGroup.isReadyForDeploy()) {
            rCore.fifoFinishMeal.add(order.customerGroup)
            if (rCore.freeWaiters.size > 0) {
                val group = rCore.fifoFinishMeal.pop()!!
                rCore.planEvent(BeginTransportMealEvent(rCore.cTime, group, rCore.freeWaiters.poll()))
            }
        }

        if (rCore.fifoOrder.size() > 0) {
            rCore.planEvent(BeginCookEvent(rCore.cTime, rCore.fifoOrder.pop()!!, rCore.freeChefs.poll()))
        }

        C.message("FINISH COOKING: ${order.orderSession.foodType.foodName()} pre ${order.customerGroup.type.desc()}")
    }

}