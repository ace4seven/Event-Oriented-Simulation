package app.events

import app.model.CustomerGroup
import app.model.Waiter
import app.stats.WaitType
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class EndTransportMealEvent(override val time: Double, val waiter: Waiter, val customerGroup: CustomerGroup): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        customerGroup.averageWaiting.stopTrack(rCore.cTime, WaitType.FORMEAL)
        rCore.stats.increaseAverage(customerGroup.averageWaiting.getResult(WaitType.FORMEAL), customerGroup.type.count())

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
            val group = rCore.fifoService.pop()!!
            rCore.planEvent(BeginOrderEvent(time, group, rCore.freeWaiters.poll()))
        } else if (rCore.fifoFinishMeal.size() > 0) {
            val meal = rCore.fifoFinishMeal.pop()!!
            rCore.planEvent(BeginTransportMealEvent(time, meal,  rCore.freeWaiters.poll()))
        } else if (rCore.fifoPayment.size() > 0) {
            val group = rCore.fifoPayment.pop()!!
            rCore.planEvent(BeginPayEvent(time, group, rCore.freeWaiters.poll()))
        }

        rCore.planEvent(EndEatingEvent(rCore.cTime + finishEatingTime, customerGroup))

        C.message("Koniec odnesenia jedla pre: ${customerGroup.type.desc()}")
    }

}