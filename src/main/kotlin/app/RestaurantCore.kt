package app

import app.model.Chef
import app.model.Customer
import app.model.Waiter
import core.EventCore
import support.OrderSession
import support.Queue
import support.TableManager
import java.util.*

class RestaurantCore(val numberOfWaiters: Int, val numberOfCooks: Int, time: Double, replications: Long): EventCore(time, replications) {

    // MARK: TABLE MANAGER
    open val tableManager = TableManager()

    // MARK: - QUEUES
    open val serviceQueue = Queue<Customer>()
    open val ordersQueue = Queue<OrderSession>()
    open val finishedMealsQueue = Queue<Customer>()
    open val paymentQueue = Queue<Customer>()

    // MARK: - Priority FRONTS
    open var freeWaiters: PriorityQueue<Waiter> = PriorityQueue()
    open var freeChefs: PriorityQueue<Chef> = PriorityQueue()



}