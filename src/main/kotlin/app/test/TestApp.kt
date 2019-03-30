package app.test

import core.RestaurantSimulationCore
import core.generators.TriangleGenerator

fun main(args: Array<String>) {
    testCore()
}

private fun testCore() {
    val rCore = RestaurantSimulationCore(4, 15, 32400.0, 10000)

    rCore.start()
}

private fun testTriangleDistribution() {

    val generator = TriangleGenerator(10.toDouble(), 100.toDouble(), 40.toDouble())

    for (i in 1..100000) {
        println(generator.nextDouble())
    }

}