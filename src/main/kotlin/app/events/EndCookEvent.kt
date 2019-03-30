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

        chef.stopWorking(time)
        rCore.freeChefs.add(chef)
        rCore.stats.averageWorkingTimesChefs[chef.getID()] = chef.getWorkingTime()

        order.customerGroup.incMeals()

        if (order.customerGroup.isReadyForDeploy()) {
            rCore.fifoFinishMeal.add(order.customerGroup)
            if (rCore.freeWaiters.size > 0) {
                val group = rCore.fifoFinishMeal.pop()!!
                rCore.planEvent(BeginTransportMealEvent(time, group, rCore.freeWaiters.poll()))
            }
        }

        if (rCore.fifoOrder.size() > 0) {
            rCore.planEvent(BeginCookEvent(time, rCore.fifoOrder.pop()!!, rCore.freeChefs.poll()))
        }
    }

    override fun debugPrint() {
        C.message("END COOKING(chef: ${chef.getID()}) " +
                "- Meal(name: ${order.orderSession.foodType.foodName()}) " +
                "for customer(id: ${order.customerGroup.getID()}, " +
                "table: ${order.customerGroup.table().type.desc()})" +
                " TIME: ${time}")
    }

}