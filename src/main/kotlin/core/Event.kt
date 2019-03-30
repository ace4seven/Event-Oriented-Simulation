package core

abstract class Event: Comparable<Event> {

    abstract val time: Double
    abstract fun execute(simulationCore: EventSimulationCore)

    override fun compareTo(other: Event): Int {
        if (time == other.time) { return 0 }
        if (time < other.time) { return -1 }

        return 1
    }

    open fun debugPrint() {}

}