package core

import app.events.ArrivalGroupEvent
import app.model.*
import app.stats.*
import core.generators.CEvenGenerator
import core.generators.ExponencialGenerator
import core.generators.TriangleGenerator
import support.FoodManager
import support.Queue
import support.TableManager
import java.util.*
import kotlin.math.max

class RestaurantSimulationCore(var numberOfWaiters: Int, var numberOfChefs: Int, time: Double, replications: Long): EventSimulationCore(time, replications) {

    private val seedGenerator = Random()

    var customerGroupID = 1

    val stats = Statistics()

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

    val globalStatistics = GlobalStatistics()
    val localStatistics = LocalStatistics()

    var stateStats = StateStatistic()

    var isPaused = false
        private set

    open fun resume() {
        isRunning = !isRunning
        isPaused =  !isPaused
    }

    open fun pause() {
        isRunning = !isRunning
        isPaused =  !isPaused
    }

    init {
        tableManager.prepareTables(stateStats)
    }

    override fun beforeSimulation(core: MCSimulationCore) {
        super.beforeSimulation(core)

        stats.simulationTimeDuration = maxTime
        initializePersonal()
        prepareForSimulation()
    }

    override fun afterSimulation(core: MCSimulationCore) {
        super.afterSimulation(core)
    }

    override fun beforeReplication(core: MCSimulationCore) {
        super.beforeReplication(core)

        stats.incReplication()
    }

    override fun afterReplication(core: MCSimulationCore) {
        super.afterReplication(core)

        if (isCooling) {
            stats.updateBusinessTime(cTime)
            stats.simulationTimeDuration = cTime
        } else {
            stats.updateBusinessTime(maxTime)
        }

        restartCTime()

        if (isFast) {
            globalStatistics.update(stats)
        }
        initializePersonal()
        prepareForSimulation()
        emptyQueues()
        tableManager.reset()

        stats.updateWithReplication()
        customerGroupID = 1
    }

    override fun clear() {
        super.clear()

        stats.reset()

        customerGroupID = 1
        initializePersonal()
        emptyQueues()
        tableManager.reset()
    }

    private fun initializePersonal() {
        freeWaiters.clear()
        freeChefs.clear()

        for (i in 1..numberOfWaiters) {
            val waiter = Waiter(i)

            stateStats.subscribeWaiter(waiter)
            freeWaiters.add(waiter)
        }
        for (i in 1..numberOfChefs) {
            val chef = Chef(i)

            stateStats.subscribeChef(chef)
            freeChefs.add(chef)
        }
    }

    private fun emptyQueues() {
        fifoService.clear()
        fifoFinishMeal.clear()
        fifoOrder.clear()
        fifoPayment.clear()
    }

    private fun prepareForSimulation() {
        planEvent(ArrivalGroupEvent(cTime + oneCustomerGenerator.nextDouble(), CustomerGroup(customerGroupID, CustomerGroupType.ONE)))

        customerGroupID += 1
        planEvent(ArrivalGroupEvent(cTime + twoCustomerGenerator.nextDouble(), CustomerGroup(customerGroupID, CustomerGroupType.TWO)))

        customerGroupID += 1
        planEvent(ArrivalGroupEvent(cTime + threeCustomerGenerator.nextDouble(), CustomerGroup(customerGroupID, CustomerGroupType.THREE)))

        customerGroupID += 1
        planEvent(ArrivalGroupEvent(cTime + fourCustomerGenerator.nextDouble(), CustomerGroup(customerGroupID, CustomerGroupType.FOUR)))

        customerGroupID += 1
        planEvent(ArrivalGroupEvent(cTime + fiveCustomerGenerator.nextDouble(), CustomerGroup(customerGroupID, CustomerGroupType.FIVE)))

        customerGroupID += 1
        planEvent(ArrivalGroupEvent(cTime + sixCustomerGenerator.nextDouble(), CustomerGroup(customerGroupID, CustomerGroupType.SIX)))
    }

    override fun afterEvent(core: EventSimulationCore) {
        stateStats.updateStates(this)
        localStatistics.update(stats, cTime)
    }

}