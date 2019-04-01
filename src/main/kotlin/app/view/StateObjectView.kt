package app.view

import app.model.*
import javafx.scene.text.FontWeight
import tornadofx.*

class StateObjectView : AppView("Stav objektov") {

    override val root = vbox {
        prefWidth = 1500.0
        prefHeight = 1000.0

        hbox {
            vbox {
                hbox {
                    label("11 : 00 : 00") {
                        minWidth = 200.0
                        style {
                            fontWeight = FontWeight.BOLD
                            fontSize = 30.px
                        }
                        hboxConstraints {
                            marginTop = 10.0
                            marginBottom = 10.0
                        }
                        bind(controller.simulationTimeProperty)
                    }

                    button("Pauza simulácie") {
                        minWidth = 150.0
                        hboxConstraints {
                            marginTop = 10.0
                            marginBottom = 10.0
                            marginLeft = 20.0
                        }
                        action {
                            controller.pauseSimulationAction()
                        }
                    }
                }

                hbox {
                    vbox {
                        label("Kalendár udalosí") {
                            vboxConstraints {
                                marginTop = 10.0
                                marginBottom = 10.0
                            }
                        }
                        tableview<CalendarData> {
                            hboxConstraints {
                                marginRight = 20.0
                            }

                            items =controller.calendarStatesDataSource
                            minWidth = 300.0

                            selectionModel.isCellSelectionEnabled = true
                            column("Sim. čas", CalendarData::time) {
                                minWidth = 90.0
                            }
                            column("Hodnota štatistiky", CalendarData::desc) {
                                minWidth = 410.0
                            }
                        }
                    }

                    vbox {
                        label("Stoly") {
                            vboxConstraints {
                                marginTop = 10.0
                                marginBottom = 10.0
                            }
                        }
                        tableview<TableData> {
                            hboxConstraints {
                                marginTop = 20.0
                            }

                            items =controller.tableDataSource
                            minWidth = 200.0

                            selectionModel.isCellSelectionEnabled = true

                            column("ID", TableData::id) {
                                maxWidth = 30.0
                            }

                            column("Kapacita", TableData::capacity) {
                                minWidth = 50.0
                            }

                            column("Stav", TableData::status) {
                                minWidth = 220.0
                            }

                        }
                    }

                    vbox {
                        label("Čašníci") {
                            vboxConstraints {
                                marginTop = 10.0
                                marginBottom = 10.0
                            }
                        }
                        tableview<WorkerData> {
                            hboxConstraints {
                                marginTop = 20.0
                            }

                            items =controller.waitersDataSource
                            minWidth = 200.0

                            selectionModel.isCellSelectionEnabled = true
                            column("ID", WorkerData::id) {
                                maxWidth = 30.0
                            }
                            column("Odpracované", WorkerData::time) {
                                minWidth = 50.0
                            }
                            column("Stav", WorkerData::status) {
                                minWidth = 220.0
                            }
                            column("Dokončené", WorkerData::completion) {
                                minWidth = 20.0
                            }
                        }
                    }

                    vbox {
                        label("Kuchári") {
                            vboxConstraints {
                                marginTop = 10.0
                                marginBottom = 10.0
                            }
                        }
                        tableview<WorkerData> {
                            hboxConstraints {
                                marginRight = 20.0
                            }

                            items =controller.chefsDataSource
                            minWidth = 200.0

                            selectionModel.isCellSelectionEnabled = true
                            column("ID", WorkerData::id) {
                                maxWidth = 30.0
                            }
                            column("Odpracované", WorkerData::time) {
                                minWidth = 50.0
                            }
                            column("Stav", WorkerData::status) {
                                minWidth = 220.0
                            }
                            column("Dokončené", WorkerData::completion) {
                                minWidth = 20.0
                            }
                        }
                    }
                }

                hbox {
                    vbox {
                        label("Čakanie na menu") {
                            vboxConstraints {
                                marginTop = 10.0
                                marginBottom = 10.0
                            }
                        }
                        tableview<WaitingData> {
                            hboxConstraints {
                                marginRight = 20.0
                            }

                            items =controller.waitingServiceDatasource
                            minWidth = 180.0

                            selectionModel.isCellSelectionEnabled = true
                            column("ID Skup.", WaitingData::id) {
                                minWidth = 90.0
                            }
                            column("Počet", WaitingData::capacity) {
                                minWidth = 90.0
                            }
                        }
                    }
                    vbox {
                        label("Čakanie na jedlo") {
                            vboxConstraints {
                                marginTop = 10.0
                                marginBottom = 10.0
                            }
                        }
                        tableview<WaitingData> {
                            hboxConstraints {
                                marginRight = 20.0
                            }

                            items =controller.waitingForMealDataSource
                            minWidth = 180.0

                            selectionModel.isCellSelectionEnabled = true
                            column("ID Skup.", WaitingData::id) {
                                minWidth = 90.0
                            }
                            column("Počet", WaitingData::capacity) {
                                minWidth = 90.0
                            }
                        }
                    }

                    vbox {
                        label("Čakanie na platbu") {
                            vboxConstraints {
                                marginTop = 10.0
                                marginBottom = 10.0
                            }
                        }
                        tableview<WaitingData> {
                            hboxConstraints {
                                marginRight = 20.0
                            }

                            items = controller.waitingPayDataSource
                            minWidth = 180.0

                            selectionModel.isCellSelectionEnabled = true
                            column("ID Skup.", WaitingData::id) {
                                minWidth = 90.0
                            }
                            column("Počet", WaitingData::capacity) {
                                minWidth = 90.0
                            }
                        }
                    }

                    vbox {
                        label("Voľný čašníci") {
                            vboxConstraints {
                                marginTop = 10.0
                                marginBottom = 10.0
                            }
                        }
                        tableview<FreeWorkerData> {
                            hboxConstraints {
                                marginRight = 20.0
                            }

                            items = controller.freeWaiterDatasource
                            minWidth = 180.0

                            selectionModel.isCellSelectionEnabled = true
                            column("ID", FreeWorkerData::id) {
                                minWidth = 90.0
                            }
                            column("Odpracované", FreeWorkerData::time) {
                                minWidth = 90.0
                            }
                        }
                    }
                    vbox {
                        label("Voľný kuchári") {
                            vboxConstraints {
                                marginTop = 10.0
                                marginBottom = 10.0
                            }
                        }
                        tableview<FreeWorkerData> {
                            hboxConstraints {
                                marginRight = 20.0
                            }

                            items = controller.freeChefDataSource
                            minWidth = 180.0

                            selectionModel.isCellSelectionEnabled = true
                            column("ID", FreeWorkerData::id) {
                                minWidth = 90.0
                            }
                            column("Odpracované", FreeWorkerData::time) {
                                minWidth = 90.0
                            }
                        }
                    }

                    vbox {
                        label("Objednávky") {
                            vboxConstraints {
                                marginTop = 10.0
                                marginBottom = 10.0
                            }
                        }
                        tableview<MealFrontData> {
                            hboxConstraints {
                                marginRight = 20.0
                            }

                            items = controller.orderedMealsDataSource
                            minWidth = 180.0

                            selectionModel.isCellSelectionEnabled = true
                            column("ID stola", MealFrontData::tableID) {
                                minWidth = 50.0
                            }
                            column("Názov", MealFrontData::name) {
                                minWidth = 150.0
                            }
                        }
                    }
                }
            }
        }
    }

}
