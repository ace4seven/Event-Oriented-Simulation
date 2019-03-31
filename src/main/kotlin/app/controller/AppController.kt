package app.controller

import app.model.StatEntry
import core.EventSimulationCore
import core.EventSimulationCoreObserver
import core.RestaurantSimulationCore
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableStringValue
import javafx.collections.FXCollections
import javafx.scene.chart.XYChart
import tornadofx.*
import app.stats.*
import javafx.collections.ObservableList
import java.lang.IndexOutOfBoundsException
import kotlin.properties.ObservableProperty

class AppController: Controller(), EventSimulationCoreObserver {

    // MARK: - Properties
    val normalModeProperty = SimpleBooleanProperty()
    var isNormalMode: Boolean by normalModeProperty

    val fastModeProperty = SimpleBooleanProperty()
    var isFastMode: Boolean by fastModeProperty

    val collingModeProperty = SimpleBooleanProperty()
    var isCooling: Boolean by collingModeProperty

    var averageWaitingChartData = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()
    var averageWaitingServiceChartData = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()
    var averageWaitingMealChartData = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()
    var averageWaitingPayChartData = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()

    val numberOfReplicationsProperty = SimpleIntegerProperty()
    val numberOfReplications: Int by numberOfReplicationsProperty

    val numberOfWaitersProperty = SimpleIntegerProperty()
    val numberOfWaiters: Int by numberOfWaitersProperty

    val numberOfChefsProperty = SimpleIntegerProperty()
    val numberOfChefs: Int by numberOfChefsProperty

    val numberOfDaysProperty = SimpleIntegerProperty()
    val numberOfDays: Int by numberOfDaysProperty

    val averageWaitingTimeProperty = SimpleStringProperty()
    var avearegWaitingTime: String by averageWaitingTimeProperty

    var mainStatsDataSource= FXCollections.observableArrayList<StatEntry>()


    var speed: Int = 0


    private var restaurantCore = RestaurantSimulationCore(0, 0, 0.0, 0)

    init {
        restaurantCore.subscribe(this)
    }

    fun startSimulationAction() {
        if (restaurantCore.isPaused) {
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

    private fun setupCore() {
        restaurantCore.numberOfWaiters = numberOfWaiters
        restaurantCore.numberOfChefs = numberOfChefs
        restaurantCore.replication = numberOfReplications.toLong()
        restaurantCore.isFast = isFastMode
        restaurantCore.isCooling = isCooling
        restaurantCore.maxTime = (if(numberOfDays == 0) 1 else(numberOfDays) * 32400).toDouble()
    }

    override fun refresh(core: EventSimulationCore) {
        mainStatsDataSource.clear()
        mainStatsDataSource.removeAll()

        val entries = restaurantCore.globalStatistics.getEntries()
        for (i in 1..entries.count()) {
            try {
                mainStatsDataSource.add(entries[i-1])
            } catch(e: IndexOutOfBoundsException) {}
        }

        val averageWaitingTimeAll = restaurantCore.stats.getAverageTimeCustomerWait().second / 60
        val averageWaitingTimeService = restaurantCore.stats.getAverageTimeCustomerWait(AverageWaitingType.SERVICE).second / 60
        val averageWaitingTimeMeal = restaurantCore.stats.getAverageTimeCustomerWait(AverageWaitingType.MEAL).second / 60
        val averageWaitingTimePay = restaurantCore.stats.getAverageTimeCustomerWait(AverageWaitingType.PAY).second / 60

        avearegWaitingTime = "${averageWaitingTimeAll} " +
                "<${if(restaurantCore.stats.getAverageTimeCustomerWait().first != null) restaurantCore.stats.getAverageTimeCustomerWait().first!! / 60 else "No data"}, " +
                "${if(restaurantCore.stats.getAverageTimeCustomerWait().first != null) restaurantCore.stats.getAverageTimeCustomerWait().third!! / 60 else "No data"}>"

        averageWaitingChartData.add(XYChart.Data(restaurantCore.currentReplication, averageWaitingTimeAll))
        averageWaitingServiceChartData.add(XYChart.Data(restaurantCore.currentReplication, averageWaitingTimeService))
        averageWaitingMealChartData.add(XYChart.Data(restaurantCore.currentReplication, averageWaitingTimeMeal))
        averageWaitingPayChartData.add(XYChart.Data(restaurantCore.currentReplication, averageWaitingTimePay))
    }

}