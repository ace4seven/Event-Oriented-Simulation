package app.events

import app.model.CustomerGroup
import app.model.Waiter
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore
import support.TableType

class EndPayEvent(override val time: Double, val waiter: Waiter, val customerGroup: CustomerGroup): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore
        val canTrackWeights = !simulationCore.isCooling || simulationCore.cTime < simulationCore.maxTime

        waiter.stopWorking(time)
        rCore.freeWaiters.add(waiter)
        if (canTrackWeights) {
            rCore.stats.freeWaitersWeight.updateChange(time, rCore.freeWaiters.size)
        }
        rCore.stats.averageFreeTimeWaiter[waiter.getID()] = waiter.getWorkingTime()

        rCore.tableManager.freeTable(customerGroup.table())
        customerGroup.table().setStatus("Voľný stôl")

        when (customerGroup.table().type) {
            TableType.TWO -> {
                if (canTrackWeights) {
                    rCore.stats.freeTableTwoWeight.updateChange(time, rCore.tableManager.twoTablesQueue.size())
                }
            }
            TableType.FOUR -> {
                if (canTrackWeights) {
                    rCore.stats.freeTableFourWeight.updateChange(time, rCore.tableManager.fourTablesQueue.size())
                }
            }
            TableType.SIX -> {
                if (canTrackWeights) {
                    rCore.stats.freeTableSixWeight.updateChange(time, rCore.tableManager.sixTablesQueue.size())
                }
            }
        }

        if (rCore.fifoService.size() > 0) {
            val group = rCore.fifoService.pop()!!
            rCore.planEvent(BeginOrderEvent(time, group, rCore.freeWaiters.poll()))
            if (canTrackWeights) {
                rCore.stats.freeWaitersWeight.updateChange(time, rCore.freeWaiters.size)
            }
        } else if (rCore.fifoFinishMeal.size() > 0) {
            val meal = rCore.fifoFinishMeal.pop()!!
            rCore.planEvent(BeginTransportMealEvent(time, meal,  rCore.freeWaiters.poll()))
            if (canTrackWeights) {
                rCore.stats.freeWaitersWeight.updateChange(time, rCore.freeWaiters.size)
            }
        } else if (rCore.fifoPayment.size() > 0) {
            val group = rCore.fifoPayment.pop()!!
            rCore.planEvent(BeginPayEvent(time, group, rCore.freeWaiters.poll()))
            if (canTrackWeights) {
                rCore.stats.freeWaitersWeight.updateChange(time, rCore.freeWaiters.size)
            }
        }
    }

    override fun debugPrint() {
        C.message("END PAY: Customer(id: ${customerGroup.getID()}, count: ${customerGroup.type.count()}) Waiter(id: ${waiter.getID()}) TIME: $time")
    }

    override fun calendarDescription(): String {
        return "Koniec platenia, skupina ${customerGroup.getID()}, obsluha: ${waiter.getID()}"
    }

}