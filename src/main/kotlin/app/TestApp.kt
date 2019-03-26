package app

import core.generators.TriangleGenerator
import support.FoodType
import support.OrderFoodGenerator
import java.util.*

fun main(args: Array<String>) {
//    testTriangleDistribution()
    testOrderingSession()
}

private fun testTriangleDistribution() {

    val generator = TriangleGenerator(10.toDouble(), 100.toDouble(), 40.toDouble())

    for (i in 1..100000) {
        println(generator.nextDouble())
    }

}

private fun testOrderingSession() {
    val orderList = OrderFoodGenerator(Random())
    val count = 1000000
    var penneCount = 0
    var spagetyCount = 0
    var cesarCount = 0

    var sum = 0
    for (i in 1..count) {
        val session = orderList.getOrder()

        when(session.foodType) {
            FoodType.SPAGETY -> spagetyCount += 1
            FoodType.PENNE -> penneCount += 1
            FoodType.CAESAR -> cesarCount += 1
        }
        sum += 1
        println("Objednavka ${session.foodType.foodName()} | obsluha: ${session.duration}")
    }

    println()
    println()
    println()

    println("PENNE probability: ${penneCount.toDouble() / sum.toDouble()}")
    println("Spaggety probability: ${spagetyCount.toDouble() / sum.toDouble()}")
    println("cezar probability: ${cesarCount.toDouble() / sum.toDouble()}")
}