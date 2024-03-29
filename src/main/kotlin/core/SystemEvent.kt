package core

class SystemEvent(override val time: Double): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        if (!simulationCore.isCooling || simulationCore.cTime < simulationCore.maxTime) {
            simulationCore.planEvent(SystemEvent(simulationCore.cTime + 1))
        }

        if (!simulationCore.isFast && !simulationCore.isTurboMode) { Thread.sleep(simulationCore.getSkipTime().toLong()) }
        C.message("System event load")
    }

    override fun calendarDescription(): String {
        return "SYSTEM EVENT"
    }

}