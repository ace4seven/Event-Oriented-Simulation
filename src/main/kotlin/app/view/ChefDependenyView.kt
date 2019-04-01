package app.view

import javafx.scene.chart.NumberAxis
import tornadofx.*

class ChefDependenyView : AppView("Závislosti kuchár") {

    override val root = vbox {
        form {
            fieldset("Parametre testu") {
                hbox(20) {
                    field("Počet replikácii") {
                        textfield {
                            bind(controller.chefDependencyReplicationProperty)
                            text = "200"
                        }
                    }
                    field("Počet kuchárov") {
                        textfield {
                            bind(controller.chefDependencyCountProperty)
                            text = "14"
                        }
                    }
                    field("Dolná hranica čašníkov") {
                        textfield {
                            bind(controller.chefDependencyLowProperty)
                            text = "1"
                        }
                    }
                    field("Horná hranica čašníkov") {
                        textfield {
                            bind(controller.chefDependencyHighProperty)
                            text = "5"
                        }
                    }
                }
            }
        }

        linechart("Závislosti čašníkov", NumberAxis(), NumberAxis()) {
            createSymbols = false
            isLegendVisible = false
            prefHeight = 400.0
            maxHeight = 600.0
            minWidth = 900.0
            animated = false
            series("", controller.chefDependencyChartData)
            with(yAxis as NumberAxis) {
                label = "Priemerné čakanie [sec]"
                isForceZeroInRange = false
                isAutoRanging = true
            }
            with(xAxis as NumberAxis) {
                xAxis.label = "Počet čašníkov"
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
                controller.startChefDependency()
            }
        }
    }
}