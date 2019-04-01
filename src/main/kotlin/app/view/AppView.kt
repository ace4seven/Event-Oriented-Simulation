package app.view

import app.controller.AppController
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import tornadofx.*

abstract class AppView(title: String) : View(title) {

    protected val controller: AppController by inject()

    protected var currentTime:Label by singleAssign()

    // TEXTFIELDS
    protected var replicationTextField: TextField by singleAssign()
    protected var waitersTextField: TextField by singleAssign()
    protected var chefsTextField: TextField by singleAssign()
    protected var numOfDaysTextField: TextField by singleAssign()

    // AVERAGE CHART
    protected var averageWaitxAxis : NumberAxis = NumberAxis()
    protected var averageWaityAxis : NumberAxis = NumberAxis()

    protected var averageWaitingChart: LineChart<Number, Number> by singleAssign()
    protected var averageWaitingChartData by singleAssign<XYChart.Series<Number, Number>>()

    protected var averageWaitingServiceChart: LineChart<Number, Number> by singleAssign()
    protected var averageWaitingServiceChartData by singleAssign<XYChart.Series<Number, Number>>()

    protected var averageWaitingMealChart: LineChart<Number, Number> by singleAssign()
    protected var averageWaitingMealChartData by singleAssign<XYChart.Series<Number, Number>>()

    protected var averageWaitingPayChart: LineChart<Number, Number> by singleAssign()
    protected var averageWaitingPayChartData by singleAssign<XYChart.Series<Number, Number>>()

    protected var startButton: Button by singleAssign()
    protected var pauseButton: Button by singleAssign()
    protected var stopButton: Button by singleAssign()
    protected var stateButton: Button by singleAssign()

    protected fun initial() {
        startButton.isDisable = false
        pauseButton.isDisable = true
        stopButton.isDisable = true
        stateButton.isDisable = true
    }

    protected fun start() {
        startButton.isDisable = true
        pauseButton.isDisable = false
        stopButton.isDisable = true

        stateButton.isDisable = replicationTextField.text.toInt() > 1

    }

    protected fun stop() {
        startButton.isDisable = false
        pauseButton.isDisable = true
        stopButton.isDisable = true
        stateButton.isDisable = true

        controller.mainStatsDataSource.clear()

        controller.averageWaitingPayChartData.clear()
        controller.averageWaitingMealChartData.clear()
        controller.averageWaitingServiceChartData.clear()
        controller.averageWaitingChartData.clear()

        controller.calendarStatesDataSource
        controller.waitersDataSource

        controller.mainStatsDataSource
        controller.calendarStatesDataSource.clear()
        controller.waitersDataSource.clear()
        controller.chefsDataSource.clear()
        controller.tableDataSource.clear()
        controller.freeWaiterDatasource.clear()
        controller.freeChefDataSource.clear()

        controller.waitingServiceDatasource.clear()
        controller.waitingPayDataSource.clear()
        controller.waitingForMealDataSource.clear()

        controller.orderedMealsDataSource.clear()

        currentTime.text = "11 : 00 : 00"
    }

    protected fun pause() {
        startButton.isDisable = false
        pauseButton.isDisable = true
        stopButton.isDisable = false
    }

}
