package app.view

import javafx.scene.control.TabPane
import tornadofx.*

class MainView : AppView("Udalostná simulácia - prevádzka: Reštaurácia FRI") {

    override val root = tabpane {
        prefWidth = 1500.0
        minWidth = 1500.0
        prefHeight = 1000.0

        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

        tab(SimulationSubView::class)
        tab(WaiterDependencyView::class)
        tab(ChefDependenyView::class)
    }

}