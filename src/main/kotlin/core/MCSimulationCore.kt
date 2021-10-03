package core

interface MCSimulationCoreDelegate {
    fun beforeSimulation(core: MCSimulationCore) {}
    fun beforeReplication(core: MCSimulationCore) {}
    fun start()
    fun clear()
    fun replication(core: MCSimulationCore)
    fun afterReplication(core: MCSimulationCore) {}
    fun afterSimulation(core: MCSimulationCore) {}
}

open class MCSimulationCore(var replication: Long): MCSimulationCoreDelegate {
    var currentReplication: Int = 0
        private set
    private val chartPoints: Int = 4000

    protected var jumpIndex: Int = 0

    private var isStoped = false

    override fun start() {
        isStoped = false
        val self = this
        val thread = object: Thread() {
            override fun run() {
                beforeSimulation(self)
                while(currentReplication < replication && !isStoped) {
                    currentReplication += 1
                    beforeReplication(self)
                    replication(self)
                    afterReplication(self)
                }
                if (!isStoped) {
                    afterSimulation(self)
                }
            }
        }
        thread.start()
    }

    override fun clear() {
        isStoped = true
        jumpIndex = 0
        currentReplication = 0
    }

    override fun replication(core: MCSimulationCore) {}
    override fun beforeSimulation(core: MCSimulationCore) {}
    override fun beforeReplication(core: MCSimulationCore) {}
    override fun afterReplication(core: MCSimulationCore) {}
    override fun afterSimulation(core: MCSimulationCore) {}

}