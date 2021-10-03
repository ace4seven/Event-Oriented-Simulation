package app.view

import javafx.scene.chart.CategoryAxis
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

        barchart("Závislosti čašníkov", CategoryAxis(), NumberAxis()) {
//            createSymbols = false
            isLegendVisible = false
            prefHeight = 400.0
            maxHeight = 600.0
            minWidth = 900.0
            animated = false
            series("Lorem ipsum", controller.chefDependencyChartData)
            with(yAxis as NumberAxis) {
                label = "Priemerné čakanie [sec]"
                isForceZeroInRange = false
                isAutoRanging = true
            }
            with(xAxis as CategoryAxis) {
                xAxis.label = "Počet čašníkov"
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
                    controller.startChefDependency()
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
