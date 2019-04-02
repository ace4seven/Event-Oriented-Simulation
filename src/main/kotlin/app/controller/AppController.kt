package app.controller

import app.model.*
import core.EventSimulationCore
import core.EventSimulationCoreObserver
import core.RestaurantSimulationCore
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.chart.XYChart
import tornadofx.*
import app.stats.*
import javafx.beans.property.SimpleDoubleProperty
import java.lang.IndexOutOfBoundsException

class AppController: Controller(), EventSimulationCoreObserver {

    // MARK: - Properties

    val simulationTimeProperty = SimpleStringProperty("11 : 00 : 00")
    var simulationTime: String by simulationTimeProperty

    val normalModeProperty = SimpleBooleanProperty()
    var isNormalMode: Boolean by normalModeProperty

    val fastModeProperty = SimpleBooleanProperty()
    var isFastMode: Boolean by fastModeProperty

    val collingModeProperty = SimpleBooleanProperty()
    var isCooling: Boolean by collingModeProperty

    val turboModeProperty = SimpleBooleanProperty()
    var isTurbo: Boolean by turboModeProperty

    val numberOfReplicationsProperty = SimpleIntegerProperty()
    val numberOfReplications: Int by numberOfReplicationsProperty

    val numberOfWaitersProperty = SimpleIntegerProperty()
    val numberOfWaiters: Int by numberOfWaitersProperty

    val numberOfChefsProperty = SimpleIntegerProperty()
    val numberOfChefs: Int by numberOfChefsProperty

    val numberOfDaysProperty = SimpleIntegerProperty()
    val numberOfDays: Int by numberOfDaysProperty

    // WAITER DEPENDENCY

    val waiterDependencyReplicationProperty = SimpleIntegerProperty()
    var waiterDependencyReplication: Int by waiterDependencyReplicationProperty

    val waiterDependencyCountProperty = SimpleIntegerProperty()
    var waiterDependencyCount: Int by waiterDependencyCountProperty

    val waiterDependencyLowProperty = SimpleIntegerProperty()
    val waiterDependencyLow:Int  by waiterDependencyLowProperty

    val waiterDependencyHighProperty = SimpleIntegerProperty()
    val waiterDependencyHigh:Int  by waiterDependencyHighProperty

    var waiterDependencyChartData = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()

    val chefDependencyReplicationProperty = SimpleIntegerProperty()
    var chefDependencyReplication: Int by chefDependencyReplicationProperty

    val chefDependencyCountProperty = SimpleIntegerProperty()
    var chefDependencyCount: Int by chefDependencyCountProperty

    val chefDependencyLowProperty = SimpleIntegerProperty()
    val chefDependencyLow:Int  by chefDependencyLowProperty

    val chefDependencyHighProperty = SimpleIntegerProperty()
    val chefDependencyHigh:Int  by chefDependencyHighProperty

    var chefDependencyChartData = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()

    // TABLES
    var mainStatsDataSource= FXCollections.observableArrayList<StatEntry>()
    var repDataSource= FXCollections.observableArrayList<StatEntry>()

    var calendarStatesDataSource= FXCollections.observableArrayList<CalendarData>()
    var waitersDataSource= FXCollections.observableArrayList<WorkerData>()
    var chefsDataSource= FXCollections.observableArrayList<WorkerData>()
    var tableDataSource = FXCollections.observableArrayList<TableData>()
    var freeWaiterDatasource = FXCollections.observableArrayList<FreeWorkerData>()
    var freeChefDataSource = FXCollections.observableArrayList<FreeWorkerData>()

    var waitingServiceDatasource = FXCollections.observableArrayList<WaitingData>()
    var waitingPayDataSource = FXCollections.observableArrayList<WaitingData>()
    var waitingForMealDataSource = FXCollections.observableArrayList<WaitingData>()

    var averageWaitingChartData = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()
    var averageWaitingServiceChartData = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()
    var averageWaitingMealChartData = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()
    var averageWaitingPayChartData = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()

    var orderedMealsDataSource = FXCollections.observableArrayList<MealFrontData>()

    var progressProperty = SimpleDoubleProperty()
    var progress: Double by progressProperty

    var isWaiterDependencySimulate = false
    var isChefDependencySimulate = false


    private var restaurantCore = RestaurantSimulationCore(0, 0, 0.0, 0)

    init {
        restaurantCore.subscribe(this)
    }

    fun startWaiterDependency() {
        waiterDependencyChartData.clear()
        isWaiterDependencySimulate = true
        isChefDependencySimulate = false

        for (i in waiterDependencyLow..waiterDependencyHigh) {
            val simCore = RestaurantSimulationCore(waiterDependencyCount, i, 32400.0, waiterDependencyReplication.toLong())
            simCore.subscribe(this)
            simCore.isTurboMode = true
            simCore.isFast = true
            simCore.start()
        }
    }

    fun startChefDependency() {
        chefDependencyChartData.clear()
        isWaiterDependencySimulate = false
        isChefDependencySimulate = true

        for (i in chefDependencyLow..chefDependencyHigh) {
            val simCore = RestaurantSimulationCore(i, chefDependencyCount, 32400.0, chefDependencyReplication.toLong())
            simCore.subscribe(this)
            simCore.isTurboMode = true
            simCore.isFast = true
            simCore.start()
        }
    }

    fun startSimulationAction() {
        isWaiterDependencySimulate = false
        isChefDependencySimulate = false

        if (restaurantCore.isPaused) {
            restaurantCore.isFast = isFastMode
            restaurantCore.resume()
        } else {
            setupCore()
            restaurantCore.start()
        }
    }

    fun pauseSimulationAction() {
        restaurantCore.pause()
    }

    fun stopSimulationAction() {
        restaurantCore = RestaurantSimulationCore(numberOfWaiters, numberOfChefs, (if(numberOfDays == 0) 1 else(numberOfDays) * 32400).toDouble(), numberOfReplications.toLong())
        restaurantCore.subscribe(this)

        setupCore()
    }

    fun changeSkipSpeed(newValue: Double) {
        restaurantCore.changeSkipTime(newValue)
    }

    private fun setupCore() {
        restaurantCore.numberOfWaiters = numberOfWaiters
        restaurantCore.numberOfChefs = numberOfChefs
        restaurantCore.replication = numberOfReplications.toLong()
        restaurantCore.isFast = isFastMode
        restaurantCore.isCooling = isCooling
        restaurantCore.isTurboMode = isTurbo
        restaurantCore.maxTime = (if(numberOfDays == 0) 1 else(numberOfDays) * 32400).toDouble()
    }

    override fun refresh(core: EventSimulationCore) {
        if (isWaiterDependencySimulate) {
            val rCore = core as RestaurantSimulationCore
            waiterDependencyChartData.add(XYChart.Data(core.numberOfChefs, rCore.stats.getAverageWaitingTime().median()))
            return
        } else if (isChefDependencySimulate) {
            val rCore = core as RestaurantSimulationCore
            chefDependencyChartData.add(XYChart.Data(core.numberOfWaiters, rCore.stats.getAverageWaitingTime().median()))
            return
        }

        if (!core.isFast) {
            simulationTime = C.timeFormatterInc(restaurantCore.cTime)
            refreshStates(restaurantCore.stateStats)
        } else {
            mainStatsDataSource.clear()
            mainStatsDataSource.removeAll()

            val entries = restaurantCore.globalStatistics.getEntries().toMutableList()
            for (i in 1..entries.count()) {
                try {
                    mainStatsDataSource.add(entries[i-1])
                } catch(e: IndexOutOfBoundsException) {
                    print("Index problem")
                }
            }

            val averageWaitingTimeAll = restaurantCore.stats.getAverageWaitingTime().median()
            val averageWaitingTimeService = restaurantCore.stats.getAverageTimeCustomerWait(AverageWaitingType.SERVICE).second
            val averageWaitingTimeMeal = restaurantCore.stats.getAverageTimeCustomerWait(AverageWaitingType.MEAL).second
            val averageWaitingTimePay = restaurantCore.stats.getAverageTimeCustomerWait(AverageWaitingType.PAY).second

            averageWaitingChartData.add(XYChart.Data(restaurantCore.currentReplication, averageWaitingTimeAll))
            averageWaitingServiceChartData.add(XYChart.Data(restaurantCore.currentReplication, averageWaitingTimeService))
            averageWaitingMealChartData.add(XYChart.Data(restaurantCore.currentReplication, averageWaitingTimeMeal))
            averageWaitingPayChartData.add(XYChart.Data(restaurantCore.currentReplication, averageWaitingTimePay))
        }
    }

    private fun refreshStates(states: StateStatistic) {
        calendarStatesDataSource.clear()
        chefsDataSource.clear()
        waitersDataSource.clear()
        tableDataSource.clear()
        freeChefDataSource.clear()
        freeWaiterDatasource.clear()

        waitingServiceDatasource.clear()
        waitingForMealDataSource.clear()
        waitingPayDataSource.clear()

        orderedMealsDataSource.clear()
        repDataSource.clear()

        val entries = restaurantCore.localStatistics.getEntries().toMutableList()
        for (i in 1..entries.count()) {
            try {
                repDataSource.add(entries[i-1])
            } catch(e: IndexOutOfBoundsException) {
                print("Index problem")
            }
        }

        val calendarEntries = states.calendarData.toMutableList()
        for (i in 1..calendarEntries.count()) { calendarStatesDataSource.add(calendarEntries[i-1]) }

        val waiterEntries = states.waitersData.toMutableList()
        for (i in 1..waiterEntries.count()) { waitersDataSource.add(waiterEntries[i-1]) }

        val chefEntries = states.chefsData.toMutableList()
        for (i in 1..chefEntries.count()) { chefsDataSource.add(chefEntries[i-1]) }

        val tableEntries = states.tablesData.toMutableList()
        for (i in 1..tableEntries.count()) { tableDataSource.add(tableEntries[i-1]) }

        val freeChefEntries = states.freeChefsData.toMutableList()
        for (i in 1..freeChefEntries.count()) { freeChefDataSource.add(freeChefEntries[i-1]) }

        val freeWaiterEntries = states.freeWaitersData.toMutableList()
        for (i in 1..freeWaiterEntries.count()) { freeWaiterDatasource.add(freeWaiterEntries[i-1]) }

        val waitingServiceEntries = states.waitingForOrder.toMutableList()
        for (i in 1..waitingServiceEntries.count()) { waitingServiceDatasource.add(waitingServiceEntries[i-1]) }

        val waitingPayMentEntries = states.waitingForPay.toMutableList()
        for (i in 1..waitingPayMentEntries.count()) { waitingPayDataSource.add(waitingPayMentEntries[i-1]) }

        val waitingMealEntries = states.waitingForFood.toMutableList()
        for (i in 1..waitingMealEntries.count()) { waitingForMealDataSource.add(waitingMealEntries[i-1]) }

        val meailsFrontEntries = states.mealFrontData.toMutableList()
        for (i in 1..meailsFrontEntries.count()) { orderedMealsDataSource.add(meailsFrontEntries[i-1]) }
    }

    override fun progress(percentage: Double) {
        this.progress = percentage
    }

}