package core

abstract class EventModel: Comparable<EventModel> {
    abstract val time: Double
    abstract fun execute(core: EventCore)

    override fun compareTo(other: EventModel): Int {
        if (time == other.time) { return 0 }
        if (time < other.time) { return -1 }

        return 1
    }

}