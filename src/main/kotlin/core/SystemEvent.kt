package core

class SystemEvent(override val time: Double): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        simulationCore.planEvent(SystemEvent(simulationCore.cTime + simulationCore.getSkipTime()))

        C.message("System event load")
    }

}