package app.stats

import app.model.*
import app.RestaurantSimulationCore
import core.SystemEvent
import support.RestaurantTable


class StateStatistic {

    var calendarData = mutableListOf<CalendarData>()
        private set
    var chefsData = mutableListOf<WorkerData>()
        private set
    var waitersData = mutableListOf<WorkerData>()
        private set

    var tablesData = mutableListOf<TableData>()
        private set

    var freeWaitersData = mutableListOf<FreeWorkerData>()
        private set
    var freeChefsData = mutableListOf<FreeWorkerData>()
        private set

    var waitingForOrder = mutableListOf<WaitingData>()
        private set
    var waitingForFood = mutableListOf<WaitingData>()
        private set
    var waitingForPay = mutableListOf<WaitingData>()
        private set
    var mealFrontData = mutableListOf<MealFrontData>()
        private set

    private var chefList = mutableListOf<Chef>()
    private var waiterList = mutableListOf<Waiter>()
    private var restaurantTables = mutableListOf<RestaurantTable>()

    fun subscribeChef(chef: Chef) {
        chefList.add(chef)
    }

    fun subscribeWaiter(waiter: Waiter) {
        waiterList.add(waiter)
    }

    fun subscribeTable(table: RestaurantTable) {
        restaurantTables.add(table)
    }

    fun updateStates(core: RestaurantSimulationCore) {
        calendarData.clear()
        chefsData.clear()
        waitersData.clear()
        tablesData.clear()
        freeChefsData.clear()
        freeWaitersData.clear()

        waitingForOrder.clear()
        waitingForPay.clear()
        waitingForFood.clear()

        mealFrontData.clear()

        val c = core.timeLine.toList().filter {
            val event = it as? SystemEvent
            event == null
        }.sortedBy {
            it.time
        }
        c.forEach {
            calendarData.add(CalendarData.make(it.time, it.calendarDescription()))
        }

        core.freeWaiters.toList().forEach {
            freeWaitersData.add(FreeWorkerData.make(it.getID(), it.getWorkingTime()))
        }

        core.freeChefs.toList().forEach {
            freeChefsData.add(FreeWorkerData.make(it.getID(), it.getWorkingTime()))
        }

        core.fifoService.items.forEach {
            waitingForOrder.add(WaitingData.make(it.getID(), it.type.count()))
        }

        core.fifoPayment.items.forEach {
            waitingForPay.add(WaitingData.make(it.getID(), it.type.count()))
        }

        core.fifoFinishMeal.items.forEach {
            waitingForFood.add(WaitingData.make(it.getID(), it.type.count()))
        }

        core.fifoOrder.items.forEach {
            mealFrontData.add(MealFrontData.make(it.orderSession.foodType.foodName(), it.customerGroup.table().id))
        }

        chefList.forEach {
            chefsData.add(WorkerData.make(it.getID(), it.getWorkingTime(), it.getStatus(), it.getCompletion(core.cTime)))
        }

        waiterList.forEach {
            waitersData.add(WorkerData.make(it.getID(), it.getWorkingTime(), it.getStatus(), it.getCompletion(core.cTime)))
        }

        restaurantTables.forEach {
            tablesData.add(TableData.make(it.id, it.type.count(), it.status))
        }

    }

}
