package app.view

import app.model.StatEntry
import javafx.scene.chart.NumberAxis
import javafx.scene.control.TabPane
import javafx.scene.control.CheckBox
import tornadofx.*
import tornadofx.Stylesheet.Companion.colorRectPane
import tornadofx.Stylesheet.Companion.headerPanel
import tornadofx.Stylesheet.Companion.tab

open class SimulationSubView : AppView("Simulácia") {

    var normalCheckBox = CheckBox()
    var fastCheckBox = CheckBox()
    var coolCheckBox = CheckBox()

    override val root = vbox {
        prefWidth = 1500.0
        prefHeight = 1000.0

        form {
            vbox(20) {
                hboxConstraints {
                    marginLeft = 100.0
                }
                fieldset("Nastavenia simulácie") {
                    hbox(20) {
                        field("Počet replikácii") {
                            textfield() {
                                bind(controller.numberOfReplicationsProperty)
                                replicationTextField = this
                                replicationTextField.promptText = "Min 30"

                                text = "10000"
                            }
                        }
                        field("Počet čašníkov") {
                            textfield {
                                bind(controller.numberOfWaitersProperty)

                                waitersTextField = this
                                waitersTextField.promptText = "Zadajte počet"

                                text = "4"
                            }
                        }
                        field("Počet kuchárov") {
                            textfield {
                                bind(controller.numberOfChefsProperty)

                                chefsTextField = this
                                chefsTextField.promptText = "Zadajte počet"

                                text = "15"
                            }
                        }
                        field("Replikácia = dni") {
                            textfield {
                                bind(controller.numberOfDaysProperty)

                                numOfDaysTextField = this
                                numOfDaysTextField.promptText = "Zadajte počet"

                                text = "1"
                            }
                        }
                    }
                }

                hbox {
                    vbox {
                        hbox {
                            normalCheckBox = checkbox("Normálny", controller.normalModeProperty) {
                                hboxConstraints {
                                    marginLeft = 10.0
                                }
                                action {
                                    checkBoxLogic(true)
                                }
                            }

                            fastCheckBox = checkbox("Zrýchlený", controller.fastModeProperty) {
                                hboxConstraints {
                                    marginLeft = 10.0
                                }
                                action {
                                    checkBoxLogic(false)
                                }
                            }

                            coolCheckBox = checkbox("Chladenie", controller.collingModeProperty) {
                                hboxConstraints {
                                    marginLeft = 10.0
                                }
                                action {
                                    checkBoxLogic()
                                }
                            }
                        }

                        startButton = button("Štart simulácie") {
                            minWidth = 150.0
                            vboxConstraints {
                                marginTop = 50.0
                            }
                            action {
                                controller.startSimulationAction()
                            }
                        }

                        pauseButton = button("Pauza simulácie") {
                            minWidth = 150.0
                            vboxConstraints {
                                marginTop = 10.0
                            }
                            action {
                                controller.pauseSimulationAction()
                            }
                        }

                        stopButton = button("Reštart simulácie") {
                            minWidth = 150.0
                            vboxConstraints {
                                marginTop = 10.0
                            }
                            action {
                                controller.stopSimulationAction()
                                averageWaitingChart.data.clear()
                                averageWaitingChartData.data.clear()
                                averageWaitingChart.series("", controller.averageWaitingChartData)

                                averageWaitingPayChart.data.clear()
                                averageWaitingPayChartData.data.clear()
                                averageWaitingPayChart.series("", controller.averageWaitingPayChartData)

                                averageWaitingServiceChart.data.clear()
                                averageWaitingServiceChartData.data.clear()
                                averageWaitingServiceChart.series("", controller.averageWaitingServiceChartData)

                                averageWaitingMealChart.data.clear()
                                averageWaitingMealChartData.data.clear()
                                averageWaitingMealChart.series("", controller.averageWaitingMealChartData)
                            }
                        }

                        label("Spomalenie simulácie") {
                            vboxConstraints {
                                marginTop = 20.0
                                marginBottom = 10.0
                            }
                        }

                        slider(0.0, 100.0) {
                            isShowTickLabels = true
                            majorTickUnit = 10.0
                            blockIncrement = 10.0
                            minorTickCount = 10
                            isSnapToTicks = true

                            valueProperty().addListener { observable, oldValue, newValue ->
                                if (oldValue.toInt() != newValue.toInt()) {
                                    controller.speed = newValue.toInt()
                                }
                            }
                        }
                    }

                    tabpane {
                        hboxConstraints { marginLeft = 20.0 }
                        maxWidth = 1000.0

                        tab("Grafy priemerné čakanie vo frontoch") {
                            vbox {
                                averageWaitingChart = linechart("Priemerné celkové čakanie v reštaurácii", NumberAxis(), NumberAxis()) {
                                    createSymbols = false
                                    isLegendVisible = false
                                    prefHeight = 400.0
                                    maxHeight = 600.0
                                    minWidth = 900.0
                                    animated = false
                                    averageWaitingChartData = series("", controller.averageWaitingChartData)
                                    with(yAxis as NumberAxis) {
                                        label = "Priemerné čakanie [min]"
                                        isForceZeroInRange = false
                                        isAutoRanging = true
                                    }
                                    with(xAxis as NumberAxis) {
                                        xAxis.label = "Číslo replikácie"
                                        isForceZeroInRange = false
                                        isAutoRanging = true
                                    }
                                }

                                hbox {
                                    averageWaitingServiceChart = linechart("Obsluha", NumberAxis(), NumberAxis()) {
                                        createSymbols = false
                                        isLegendVisible = false
                                        prefHeight = 250.0
                                        maxHeight = 250.0
                                        minWidth = 250.0
                                        animated = false
                                        averageWaitingServiceChartData = series("", controller.averageWaitingServiceChartData)
                                        with(yAxis as NumberAxis) {
                                            label = "Priemerné čakanie [min]"
                                            isForceZeroInRange = false
                                            isAutoRanging = true
                                        }
                                        with(xAxis as NumberAxis) {
                                            xAxis.label = "Číslo replikácie"
                                            isForceZeroInRange = false
                                            isAutoRanging = true
                                        }
                                    }
                                    averageWaitingMealChart = linechart("Jedlo", NumberAxis(), NumberAxis()) {
                                        createSymbols = false
                                        isLegendVisible = false
                                        prefHeight = 250.0
                                        maxHeight = 250.0
                                        minWidth = 250.0
                                        animated = false
                                        averageWaitingMealChartData = series("", controller.averageWaitingMealChartData)
                                        with(yAxis as NumberAxis) {
                                            label = "Priemerné čakanie [min]"
                                            isForceZeroInRange = false
                                            isAutoRanging = true
                                        }
                                        with(xAxis as NumberAxis) {
                                            xAxis.label = "Číslo replikácie"
                                            isForceZeroInRange = false
                                            isAutoRanging = true
                                        }
                                    }

                                    averageWaitingPayChart = linechart("Platenie", NumberAxis(), NumberAxis()) {
                                        createSymbols = false
                                        isLegendVisible = false
                                        prefHeight = 250.0
                                        maxHeight = 250.0
                                        minWidth = 250.0
                                        animated = false
                                        averageWaitingPayChartData = series("", controller.averageWaitingPayChartData)
                                        with(yAxis as NumberAxis) {
                                            label = "Priemerné čakanie [min]"
                                            isForceZeroInRange = false
                                            isAutoRanging = true
                                        }
                                        with(xAxis as NumberAxis) {
                                            xAxis.label = "Číslo replikácie"
                                            isForceZeroInRange = false
                                            isAutoRanging = true
                                        }
                                    }
                                }
                            }
                        }
                        tab("Výsledky simulácie") {
                            hbox {
                                tableview<StatEntry> {
                                    hboxConstraints {
                                        marginTop = 20.0
                                    }

                                    items =controller.mainStatsDataSource
                                    minWidth = 900.0

                                    selectionModel.isCellSelectionEnabled = true
                                    column("Názov štatistiky", StatEntry::id) {
                                        minWidth = 350.0
                                    }
                                    column("Hodnota štatistiky", StatEntry::value) {
                                        minWidth = 550.0
                                    }
                                }
//                                label("Výsledky: ")
//                                label {
//                                    bind(controller.averageWaitingTimeProperty, true)
//                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkBoxLogic(isNormal: Boolean? = null) {
        if (isNormal != null) {
            if (isNormal) {
                fastCheckBox.isSelected = false
                normalCheckBox.isSelected = true
            } else {
                fastCheckBox.isSelected = true
                normalCheckBox.isSelected = false
            }
        }
    }

}
