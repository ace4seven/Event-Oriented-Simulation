package app.stats

import app.model.*
import core.RestaurantSimulationCore
import core.SystemEvent


class StateStatistic {

    var calendarData = mutableListOf<CalendarData>()
        private set
    private var chefsData = mutableListOf<WorkerData>()
    private var waitersData = mutableListOf<WorkerData>()

    private var waitingForOrder = mutableListOf<WaitingData>()
    private var waitingForFood = mutableListOf<WaitingData>()
    private var waitingForPay = mutableListOf<WaitingData>()

    private var freeWaitersData = mutableListOf<FreeWorkerData>()
    private var freeChefsData = mutableListOf<FreeWorkerData>()

    private var tablesData = mutableListOf<TableData>()
    private var mealFrontData = mutableListOf<MealFrontData>()

    fun updateStates(core: RestaurantSimulationCore) {
        calendarData.clear()
        val c = core.timeLine.toList().filter {
            val event = it as? SystemEvent
            event == null
        }
        c.forEach {
            calendarData.add(CalendarData.make(it.time, it.calendarDescription()))
        }
    }

}
