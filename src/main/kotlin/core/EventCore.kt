import core.MCSimulationCore
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

    fun planEvent(event: EventModel) {
        if (event.time < cTime) {
            throw Exception("Porušená časová kauzalita")
        }
        timeLine.add(event)
    }

    override fun beforeSimulation(core: MCSimulationCore) {

    }

    override fun beforeIteration(core: MCSimulationCore) {

    }

    override fun replication(core: MCSimulationCore) {
        simulate()
    }

    override fun afterIteration(core: MCSimulationCore) {

    }

    override fun afterSimulation(core: MCSimulationCore) {

    }

}