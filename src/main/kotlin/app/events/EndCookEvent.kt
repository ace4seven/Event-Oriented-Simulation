package app.events

import app.model.Chef
import app.model.Order
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class EndCookEvent(override val time: Double, val chef: Chef, val order: Order): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        chef.stopWorking(rCore.cTime)
        rCore.freeChefs.add(chef)

        if (order.customerGroup.isReadyForDeploy()) {
            rCore.fifoFinishMeal.add(order.customerGroup)
            if (rCore.freeWaiters.size > 0) {
                rCore.planEvent(BeginTransportMealEvent(rCore.cTime, rCore.freeWaiters.poll()))
            }
        }

        if (rCore.fifoOrder.size() > 0) {
            rCore.planEvent(BeginCookEvent(rCore.cTime, rCore.freeChefs.poll()))
        }

        C.message("Koniec varenia: ${order.orderSession.foodType.foodName()} pre ${order.customerGroup.type.desc()}")
    }

}