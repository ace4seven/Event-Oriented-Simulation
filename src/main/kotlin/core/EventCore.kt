package core

import java.lang.Exception
import java.util.*

abstract class EventCore(val maxTime: Double, replications: Long): MCSimulationCore(replications) {

    private var timeLine: PriorityQueue<EventModel> = PriorityQueue()

    var cTime: Double = 0.0
        private set

    private var isRunning: Boolean = true

    private fun simulate() {
        while (timeLine.size > 0 && cTime < maxTime && isRunning) {
            val cEvent = timeLine.poll()
            cTime = cEvent.time
            if (cTime >= maxTime) { break }

            cEvent.execute(this)
        }
    }

    open fun planEvent(event: EventModel) {
        if (event.time < cTime) {
            throw Exception("Porušená časová kauzalita")
        }
        timeLine.add(event)
    }

    override fun beforeSimulation(core: MCSimulationCore) {
        println("Before simulation")
    }

    override fun beforeIteration(core: MCSimulationCore) {
        println("Before iteration")
    }

    override fun replication(core: MCSimulationCore) {
        println("Replication")
        simulate()
    }

    override fun afterIteration(core: MCSimulationCore) {
        println("After iteration")
    }

    override fun afterSimulation(core: MCSimulationCore) {
        println("After simulation")
    }

}