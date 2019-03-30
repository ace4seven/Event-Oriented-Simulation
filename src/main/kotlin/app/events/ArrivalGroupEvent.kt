package app.events
import app.model.CustomerGroup
import app.model.CustomerGroupType
import app.stats.WaitType
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

class ArrivalGroupEvent(override val time: Double, val customerGroup: CustomerGroup): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        customerGroup.averageWaiting.startTrack(rCore.cTime, WaitType.FORSERVICE)
        rCore.stats.customerIn(customerGroup.type.count())

        var nextCome = 0.0
        when (customerGroup.type) {
            CustomerGroupType.ONE -> nextCome = rCore.oneCustomerGenerator.nextDouble()
            CustomerGroupType.TWO -> nextCome = rCore.twoCustomerGenerator.nextDouble()
            CustomerGroupType.THREE -> nextCome = rCore.threeCustomerGenerator.nextDouble()
            CustomerGroupType.FOUR -> nextCome = rCore.fourCustomerGenerator.nextDouble()
            CustomerGroupType.FIVE -> nextCome = rCore.fiveCustomerGenerator.nextDouble()
            CustomerGroupType.SIX -> nextCome = rCore.sixCustomerGenerator.nextDouble()
        }

        rCore.customerGroupID += 1
        rCore.planEvent(ArrivalGroupEvent(nextCome + simulationCore.cTime, CustomerGroup(rCore.customerGroupID, customerGroup.type)))

        val freeTable = rCore.tableManager.findedTable(customerGroup.type)

        if (freeTable == null) {
            rCore.stats.leaveCustomer(customerGroup.type.count())
            C.message("ODCHOD: ${customerGroup.type.desc()}")
        } else {
            customerGroup.addTable(freeTable)
            if (rCore.freeWaiters.size > 0) {
                val waiter = rCore.freeWaiters.poll()

                rCore.planEvent(BeginOrderEvent(rCore.cTime, customerGroup, waiter))
            } else {
                rCore.fifoService.add(customerGroup)
                C.message("${customerGroup.type.desc()} FIFO: SERVICE: ${customerGroup.type.desc()}")
            }
        }
    }

}