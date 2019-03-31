package app.stats

import app.model.*


class StateStatistic {

    private var calendarData = mutableListOf<CalendarData>()
    private var chefsData = mutableListOf<WorkerData>()
    private var waitersData = mutableListOf<WorkerData>()

    private var waitingForOrder = mutableListOf<WaitingData>()
    private var waitingForFood = mutableListOf<WaitingData>()
    private var waitingForPay = mutableListOf<WaitingData>()

    private var freeWaitersData = mutableListOf<FreeWorkerData>()
    private var freeChefsData = mutableListOf<FreeWorkerData>()

    private var tablesData = mutableListOf<TableData>()
    private var mealFrontData = mutableListOf<MealFrontData>()

}
