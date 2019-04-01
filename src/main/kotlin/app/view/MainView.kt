package app.view

import javafx.scene.control.TabPane
import tornadofx.*

class MainView : AppView("Udalostná simulácia - prevádzka: Reštaurácia FRI") {

    override val root = tabpane {
        prefWidth = 1500.0
        minWidth = 1500.0
        prefHeight = 1000.0

        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

        val firstTab = tab(SimulationSubView::class)
//        tab(GrafyZavislosti::class)
//        tab(TestSimulacie::class)
//        tab(TestGeneratorovRP::class)
    }

}