package app.events

import app.model.CustomerGroup
import app.model.Waiter
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class EndTransportMealEvent(override val time: Double, val waiter: Waiter, val customerGroup: CustomerGroup): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        waiter.stopWorking(rCore.cTime)
        rCore.freeWaiters.add(waiter)

        var finishEatingTime = 0.0
        for (i in 1..customerGroup.type.count()) {
            val customerEatingTime = rCore.eatFoodGenerator.nextDouble()
            if (finishEatingTime < customerEatingTime) {
                finishEatingTime += customerEatingTime
            }
        }

        if (rCore.fifoService.size() > 0) {
            rCore.planEvent(BeginOrderEvent(time, rCore.fifoService.pop()!!, rCore.freeWaiters.poll()))
        } else if (rCore.fifoFinishMeal.size() > 0) {
            rCore.planEvent(BeginTransportMealEvent(time,  rCore.freeWaiters.poll()))
        } else if (rCore.fifoPayment.size() > 0) {
            rCore.planEvent(BeginPayEvent(time, rCore.fifoPayment.pop()!!, rCore.freeWaiters.poll()))
        }

        rCore.planEvent(EndEatingEvent(rCore.cTime + finishEatingTime, customerGroup))

        C.message("Koniec odnesenia jedla pre: ${customerGroup.type.desc()}")
    }

}