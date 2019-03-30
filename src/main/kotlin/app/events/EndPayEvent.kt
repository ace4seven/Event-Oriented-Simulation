package app.events

import app.model.CustomerGroup
import app.model.Waiter
import app.stats.WaitType
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class EndPayEvent(override val time: Double, val waiter: Waiter, val customerGroup: CustomerGroup): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        waiter.stopWorking(time)
        rCore.freeWaiters.add(waiter)
        rCore.stats.averageWorkingTimesWaiters[waiter.getID()] = waiter.getWorkingTime()

        rCore.tableManager.freeTable(customerGroup.table())

        if (rCore.fifoService.size() > 0) {
            val group = rCore.fifoService.pop()!!
            rCore.planEvent(BeginOrderEvent(time, group, rCore.freeWaiters.poll()))
        } else if (rCore.fifoFinishMeal.size() > 0) {
            val meal = rCore.fifoFinishMeal.pop()!!
            rCore.planEvent(BeginTransportMealEvent(time, meal,  rCore.freeWaiters.poll()))
        } else if (rCore.fifoPayment.size() > 0) {
            val group = rCore.fifoPayment.pop()!!
            rCore.planEvent(BeginPayEvent(time, group, rCore.freeWaiters.poll()))
        }
    }

    override fun debugPrint() {
        C.message("END PAY: Customer(id: ${customerGroup.getID()}, count: ${customerGroup.type.count()}) Waiter(id: ${waiter.getID()}) TIME: $time")
    }

}