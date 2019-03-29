package app.test

import core.RestaurantSimulationCore
import core.generators.TriangleGenerator

fun main(args: Array<String>) {
    testCore()
}

private fun testCore() {
    val rCore = RestaurantSimulationCore(1, 5, 3240000.0, 1)

    rCore.start()
}

private fun testTriangleDistribution() {

    val generator = TriangleGenerator(10.toDouble(), 100.toDouble(), 40.toDouble())

    for (i in 1..100000) {
        println(generator.nextDouble())
    }

}