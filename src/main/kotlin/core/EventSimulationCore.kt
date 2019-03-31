package core

import javafx.application.Platform
import java.lang.Exception
import java.util.*

interface EventSimulationCoreObserver {
    fun refresh(core: EventSimulationCore)
}

abstract class EventSimulationCore(var maxTime: Double, replications: Long): MCSimulationCore(replications) {

    private var timeLine: PriorityQueue<Event> = PriorityQueue()
    private var skipTime: Double = 100.0

    var isCooling = false
    var isFast = false

    private var observers = arrayListOf<EventSimulationCoreObserver>()

    var cTime: Double = 0.0
        private set

    protected var isRunning: Boolean = true

    private fun simulate() {
        while (timeLine.size > 0 && cTime < maxTime) {

            while (!isRunning) {
                Thread.sleep(1000)
            }

            val cEvent = timeLine.poll()

            if (C.DEBUG) { cEvent.debugPrint() }

            cTime = cEvent.time
            if (cTime >= maxTime) { break }

            cEvent.execute(this)

            if (!isFast) { updateGUI() }
        }
    }

    private fun updateGUI() {
        observers.forEach {
            Platform.runLater(Runnable {
                it.refresh(this)
            })
        }
        Thread.sleep(1)
    }

    open fun subscribe(controller: EventSimulationCoreObserver) {
        observers.add(controller)
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

    override fun replication(core: MCSimulationCore) {
        simulate()
    }

    override fun afterReplication(core: MCSimulationCore) {
        cTime = 0.0
        timeLine.clear()

        if (isFast) {
            if (currentReplication.toDouble() / replication.toDouble() >= 0.1 && canUpdateGui()) {
                updateGUI()
            }
        }
    }

    override fun clear() {
        cTime = 0.0
        timeLine.clear()
    }

    override fun beforeSimulation(core: MCSimulationCore) {
        planEvent(SystemEvent(cTime))
    }

    override fun afterSimulation(core: MCSimulationCore) {
        println("Simulation finished")
    }

    private fun canUpdateGui():Boolean {
        if (replication < 4000) {
            return true
        }
        val factor = replication / 4000

        return currentReplication.toLong() % factor == 0.toLong()
    }

}