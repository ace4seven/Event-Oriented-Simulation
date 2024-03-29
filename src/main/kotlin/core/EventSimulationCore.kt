package core

import javafx.application.Platform
import java.lang.Exception
import java.util.*

interface EventSimulationCoreObserver {
    fun refresh(core: EventSimulationCore)
    fun progress(percentage: Double)
}

abstract class EventSimulationCore(var maxTime: Double, replications: Long): MCSimulationCore(replications) {

    var timeLine: PriorityQueue<Event> = PriorityQueue()
        private set

    private var skipTime: Double = 1.0
    private var progress: Double = 0.0
    private var observers = arrayListOf<EventSimulationCoreObserver>()

    var isCooling = false
    var isFast = false
    var isTurboMode = false

    var cTime: Double = 0.0
        private set

    protected var isRunning: Boolean = true

    open fun beforeEvent(core: EventSimulationCore) {}
    open fun afterEvent(core: EventSimulationCore) {}

    open fun subscribe(controller: EventSimulationCoreObserver) {
        observers.add(controller)
    }

    open fun changeSkipTime(time: Double) {
        skipTime = Math.pow(time, 0.5)
    }

    open fun getSkipTime(): Double {
        return skipTime
    }

    open fun restartCTime() {
        cTime = 0.0
    }

    open fun planEvent(event: Event) {
        if (event.time < cTime) {
            throw Exception("Porušená časová kauzalita")
        }
        timeLine.add(event)
    }

    override fun replication(core: MCSimulationCore) {
        super.replication(core)

        simulate()
    }

    override fun afterReplication(core: MCSimulationCore) {
        super.afterReplication(core)

        timeLine.clear()
        if (isFast && !isTurboMode) {
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
        updateGUI()
    }

    private fun canUpdateGui():Boolean {
        if (replication < 4000) {
            return true
        }
        val factor = replication / 4000

        return currentReplication.toLong() % factor == 0.toLong()
    }

    private fun simulate() {
        while (timeLine.size > 0 && ((cTime <= maxTime) || isCooling)) {

            while (!isRunning) {
                Thread.sleep(100)
            }

            val cEvent = timeLine.poll()

            cTime = cEvent.time

            updateProgress()

            if ((cTime > maxTime) && !isCooling) { break }

            cEvent.execute(this)

            if (!isFast) {
                afterEvent(this)
            }

            if (!isTurboMode && !isFast) {
                updateGUI()
            }
        }
    }

    private fun updateProgress() {
        if (isFast && replication > 0) {
            val status = currentReplication.toDouble() / replication.toDouble() * 100
            if (status > progress) {
                progress += 1
                observers.forEach {
                    it.progress(progress / 100.0)
                }
            }
        } else {
            val status = cTime.toDouble() / maxTime * 100
            if (status > progress) {
                progress += 1
                observers.forEach {
                    it.progress(progress / 100.0)
                }
            }
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

}