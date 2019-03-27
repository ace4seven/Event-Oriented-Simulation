package app

import app.model.Chef
import app.model.Customer
import app.model.Waiter
import core.EventCore
import core.generators.CEvenGenerator
import core.generators.ExponencialGenerator
import core.generators.TriangleGenerator
import support.FoodManager
import support.OrderSession
import support.Queue
import support.TableManager
import java.util.*

class RestaurantCore(val numberOfWaiters: Int, val numberOfCooks: Int, time: Double, replications: Long): EventCore(time, replications) {

    private val seedGenerator = Random()

    // MARK: MANAGERS
    val tableManager = TableManager()
    val foodManager = FoodManager(seedGenerator)

    // MARK: - QUEUES
    val serviceQueue = Queue<Customer>()
    val ordersQueue = Queue<OrderSession>()
    val finishedMealsQueue = Queue<Customer>()
    val paymentQueue = Queue<Customer>()

    // MARK: - Priority FRONTS
    var freeWaiters: PriorityQueue<Waiter> = PriorityQueue()
    var freeChefs: PriorityQueue<Chef> = PriorityQueue()

    // MARK: - Generators
    val oneCustomerGenerator = ExponencialGenerator(1.0 / 360.0, seedGenerator.nextLong())
    val twoCustomerGenerator = ExponencialGenerator(1.0 / 450.0, seedGenerator.nextLong())
    val threeCustomerGenerator = ExponencialGenerator(1.0 / 600.0, seedGenerator.nextLong())
    val fourCustomerGenerator = ExponencialGenerator(1.0 / 720.0, seedGenerator.nextLong())
    val fiveCustomerGenerator = ExponencialGenerator(1.0 / 1200.0, seedGenerator.nextLong())
    val sixCustomerGenerator = ExponencialGenerator(1.0 / 900.0, seedGenerator.nextLong())
    val serviceGenerator = CEvenGenerator(45.0, 120.0, seedGenerator.nextLong())
    val eatFoodGenerator = TriangleGenerator(180.0, 1800.0, 900.0, seedGenerator.nextLong())
    val payGenerator = CEvenGenerator(43.0, 97.0, seedGenerator.nextLong())
    val durationFoodToCustomerGenerator = CEvenGenerator(23.0, 80.0, seedGenerator.nextLong())

}