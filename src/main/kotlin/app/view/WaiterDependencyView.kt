package app.view

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

        linechart("Závislosti kuchárov", NumberAxis(), NumberAxis()) {
            createSymbols = false
            isLegendVisible = false
            prefHeight = 400.0
            maxHeight = 600.0
            minWidth = 900.0
            animated = false
            series("", controller.waiterDependencyChartData)
            with(yAxis as NumberAxis) {
                label = "Priemerné čakanie [min]"
                isForceZeroInRange = false
                isAutoRanging = true
            }
            with(xAxis as NumberAxis) {
                xAxis.label = "Počet kuchárov"
                isForceZeroInRange = false
                isAutoRanging = true
            }
        }

        button("Spustiť ") {
            minWidth = 150.0
            vboxConstraints {
                marginTop = 50.0
                marginLeft = 20.0
            }
            action {
                controller.startWaiterDependency()
            }
        }
    }

}
