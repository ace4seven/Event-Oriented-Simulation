package core

import java.lang.Exception
import java.util.*

abstract class EventSimulationCore(val maxTime: Double, replications: Long): MCSimulationCore(replications) {

    private var timeLine: PriorityQueue<Event> = PriorityQueue()
    private var skipTime: Double = 100.0

    var cTime: Double = 0.0
        private set

    private var isRunning: Boolean = true

    init {
        planEvent(SystemEvent(cTime))
    }

    private fun simulate() {
        while (timeLine.size > 0 && cTime < maxTime && isRunning) {
            val cEvent = timeLine.poll()
            cTime = cEvent.time
            if (cTime >= maxTime) { break }

            cEvent.execute(this)
        }
    }

    open fun changeSkipTime(time: Double) {
        skipTime = time
    }

    open fun getSkipTime(): Double {
        return skipTime
    }

    open fun planEvent(event: Event) {
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
        simulate()
    }

    override fun afterIteration(core: MCSimulationCore) {
        println("After iteration")
    }

    override fun afterSimulation(core: MCSimulationCore) {

    }

}