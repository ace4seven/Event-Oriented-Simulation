package core

import app.events.ArrivalGroupEvent
import app.model.*
import core.generators.CEvenGenerator
import core.generators.ExponencialGenerator
import core.generators.TriangleGenerator
import support.FoodManager
import support.OrderSession
import support.Queue
import support.TableManager
import java.util.*

class RestaurantSimulationCore(val numberOfWaiters: Int, val numberOfChefs: Int, time: Double, replications: Long): EventSimulationCore(time, replications) {

    private val seedGenerator = Random()

    // MARK: MANAGERS
    val tableManager = TableManager()
    val foodManager = FoodManager(seedGenerator)

    // MARK: - QUEUES
    val fifoService = Queue<CustomerGroup>()
    val fifoOrder = Queue<Order>()
    val fifoFinishMeal = Queue<CustomerGroup>()
    val fifoPayment = Queue<CustomerGroup>()

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

    init {
        for (i in 1..numberOfWaiters) { freeWaiters.add(Waiter()) }
        for (i in 1..numberOfChefs) { freeChefs.add(Chef()) }

        planEvent(ArrivalGroupEvent(cTime, CustomerGroup(CustomerGroupType.ONE)))
        planEvent(ArrivalGroupEvent(cTime, CustomerGroup(CustomerGroupType.TWO)))
        planEvent(ArrivalGroupEvent(cTime, CustomerGroup(CustomerGroupType.THREE)))
        planEvent(ArrivalGroupEvent(cTime, CustomerGroup(CustomerGroupType.FOUR)))
        planEvent(ArrivalGroupEvent(cTime, CustomerGroup(CustomerGroupType.FIVE)))
        planEvent(ArrivalGroupEvent(cTime, CustomerGroup(CustomerGroupType.SIX)))
    }

}