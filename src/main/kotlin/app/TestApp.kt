package app

import core.generators.TriangleGenerator

fun main(args: Array<String>) {
    testTriangleDistribution()
}

private fun testTriangleDistribution() {

    val generator = TriangleGenerator(10.toDouble(), 100.toDouble(), 40.toDouble())

    for (i in 1..100000) {
        println(generator.nextDouble())
    }

}