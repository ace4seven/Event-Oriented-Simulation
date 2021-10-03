package app.events

import app.model.Chef
import app.model.Order
import core.Event
import core.EventSimulationCore
import app.RestaurantSimulationCore

class EndCookEvent(override val time: Double, val chef: Chef, val order: Order): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore
        val canTrackWeights = !simulationCore.isCooling || simulationCore.cTime < simulationCore.maxTime

        chef.stopWorking(time)
        rCore.stats.averageFreeTimeChef[chef.getID()] = chef.getWorkingTime()

        if (canTrackWeights) {
            rCore.stats.freeChefssWeight.addValue(time, rCore.freeChefs.size)
        }
        rCore.freeChefs.add(chef)

        order.customerGroup.incMeals()

        order.customerGroup.table().status = "Skupina ${order.customerGroup.getID()} jedlá (${order.customerGroup.getFinishedMeals()}/${order.customerGroup.type.count()})"

        if (order.customerGroup.isReadyForDeploy()) {
            rCore.fifoFinishMeal.add(order.customerGroup)
            if (rCore.freeWaiters.size > 0) {
                val group = rCore.fifoFinishMeal.pop()!!
                if (canTrackWeights) {
                    rCore.stats.freeChefssWeight.addValue(time, rCore.freeWaiters.size)
                }
                rCore.planEvent(BeginTransportMealEvent(time, group, rCore.freeWaiters.poll()))
            }
        }

        if (rCore.fifoOrder.size() > 0) {
            if (canTrackWeights) {
                rCore.stats.freeChefssWeight.addValue(time, rCore.freeChefs.size)
            }
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

    override fun calendarDescription(): String {
        return "Koniec varenia: jedlo - ${order.orderSession.foodType.foodName()}, kuchar: ${chef.getID()}, skupina: ${order.customerGroup.getID()}"
    }

}