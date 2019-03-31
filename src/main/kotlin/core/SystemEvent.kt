package core

class SystemEvent(override val time: Double): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        simulationCore.planEvent(SystemEvent(simulationCore.cTime + 10))

        if (!simulationCore.isFast) { Thread.sleep(simulationCore.getSkipTime().toLong()) }
        C.message("System event load")
    }

    override fun calendarDescription(): String {
        return "SYSTEM EVENT"
    }

}