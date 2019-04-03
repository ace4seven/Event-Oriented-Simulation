package app.view

import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import tornadofx.*

class WaiterDependencyView : AppView("Závislosti čašník") {

    override val root = vbox {
        form {
            fieldset("Parametre testu") {
                hbox(20) {
                    field("Počet replikácii") {
                        textfield {
                            waiterDepreplicationTextField = this
                            bind(controller.waiterDependencyReplicationProperty)
                            waiterDepreplicationTextField.promptText = "Min 30"
                            text = "200"
                        }
                    }
                    field("Počet čašníkov") {
                        textfield {
                            waiterDepCount = this
                            bind(controller.waiterDependencyCountProperty)
                            waiterDepCount.promptText = "Zadajte počet"
                            text = "4"
                        }
                    }
                    field("Dolná hranica kuchárov") {
                        textfield {
                            waiterDepLow = this
                            bind(controller.waiterDependencyLowProperty)
                            waiterDepLow.promptText = "Zadajte počet"

                            text = "10"
                        }
                    }
                    field("Horná hranica kuchárov") {
                        textfield {
                            waiterDepHigh = this
                            bind(controller.waiterDependencyHighProperty)
                            waiterDepHigh.promptText = "Zadajte počet"

                            text = "20"
                        }
                    }
                }
            }
        }

        barchart("Závislosti kuchárov", CategoryAxis(), NumberAxis()) {
            isLegendVisible = false
            prefHeight = 400.0
            maxHeight = 600.0
            minWidth = 900.0
            animated = false
            series("", controller.waiterDependencyChartData)
            with(yAxis as NumberAxis) {
                label = "Priemerné čakanie [sec]"
                isForceZeroInRange = false
                isAutoRanging = true
            }
            with(xAxis as CategoryAxis) {
                xAxis.label = "Počet kuchárov"
                isAutoRanging = true
            }
        }

        hbox {
            button("Spustiť ") {
                minWidth = 150.0
                hboxConstraints {
                    marginTop = 50.0
                    marginLeft = 20.0
                }
                action {
                    controller.startWaiterDependency()
                }
            }
            button("Export závislostí ") {
                minWidth = 150.0
                hboxConstraints {
                    marginTop = 50.0
                    marginLeft = 20.0
                }
                action {
                    controller.makeDependencyExport()
                    startExport()

                    isDisable = true
                }
            }
        }
    }

}
