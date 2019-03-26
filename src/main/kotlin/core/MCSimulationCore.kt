interface MCSimulationCoreDelegate {
    fun beforeSimulation(core: MCSimulationCore) {}
    fun beforeIteration(core: MCSimulationCore) {}
    fun start()
    fun pause()
    fun clear()
    fun replication(core: MCSimulationCore)
    fun afterIteration(core: MCSimulationCore) {}
    fun afterSimulation(core: MCSimulationCore) {}
}

open class MCSimulationCore(var replication: Long): MCSimulationCoreDelegate {
    var currentReplication: Int = 0
        private set

    private var simulationInProgress: Boolean = false
    private val chartPoints: Int = 4000

    protected var jumpDrawOnChart: Int = 0
    protected var jumpIndex: Int = 0

    override fun start() {
        jumpDrawOnChart = (replication / chartPoints).toInt()
        simulationInProgress = true
        val self = this
        beforeSimulation(this)
        val thread = object: Thread() {
            override fun run() {
                while(currentReplication < replication && simulationInProgress ) {
                    currentReplication += 1
                    beforeIteration(self)
                    replication(self)
                    afterIteration(self)
                }
            }
        }
        thread.start()

        afterSimulation(this)
    }

    override fun pause() {
        simulationInProgress = false
        start()
    }

    override fun clear() {
        jumpDrawOnChart = 0
        jumpIndex = 0
        currentReplication = 0
        simulationInProgress = false
    }

    override fun replication(core: MCSimulationCore) {}
    override fun beforeSimulation(core: MCSimulationCore) {}
    override fun beforeIteration(core: MCSimulationCore) {}
    override fun afterIteration(core: MCSimulationCore) {}
    override fun afterSimulation(core: MCSimulationCore) {}

}