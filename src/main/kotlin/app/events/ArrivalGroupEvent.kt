package app.events
import app.model.CustomerGroup
import app.model.CustomerGroupType
import app.stats.WaitType
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore
import support.TableType

class ArrivalGroupEvent(override val time: Double, val customerGroup: CustomerGroup): Event() {

    override fun execute(simulationCore: EventSimulationCore) {
        val rCore = simulationCore as RestaurantSimulationCore

        var nextCome = 0.0
        when (customerGroup.type) {
            CustomerGroupType.ONE -> nextCome = rCore.oneCustomerGenerator.nextDouble()
            CustomerGroupType.TWO -> nextCome = rCore.twoCustomerGenerator.nextDouble()
            CustomerGroupType.THREE -> nextCome = rCore.threeCustomerGenerator.nextDouble()
            CustomerGroupType.FOUR -> nextCome = rCore.fourCustomerGenerator.nextDouble()
            CustomerGroupType.FIVE -> nextCome = rCore.fiveCustomerGenerator.nextDouble()
            CustomerGroupType.SIX -> nextCome = rCore.sixCustomerGenerator.nextDouble()
        }

        if (!rCore.isCooling || rCore.cTime < rCore.maxTime) {
            rCore.customerGroupID += 1
            rCore.planEvent(ArrivalGroupEvent(nextCome + time, CustomerGroup(rCore.customerGroupID, customerGroup.type)))
        } else {
            return
        }

        rCore.stats.customerIn(customerGroup.type.count())

        val freeTable = rCore.tableManager.findedTable(customerGroup.type)

        if (freeTable == null) {
            rCore.stats.leaveCustomer(customerGroup.type.count())

            C.message("     !!!NO FREE TABLE Customer(id:${customerGroup.getID()}, count: ${customerGroup.type.count()}) ${rCore.tableManager.getTablesStatus()}")
        } else {
            if (!simulationCore.isCooling || simulationCore.cTime < simulationCore.maxTime) {
                when (freeTable.type) {
                    TableType.TWO -> {
                        rCore.stats.freeTableTwoWeight.addValue(time, rCore.tableManager.twoTablesQueue.size())
                    }
                    TableType.FOUR -> {
                        rCore.stats.freeTableFourWeight.addValue(time, rCore.tableManager.fourTablesQueue.size())
                    }
                    TableType.SIX -> {
                        rCore.stats.freeTableSixWeight.addValue(time, rCore.tableManager.sixTablesQueue.size())
                    }
                }
            }

            customerGroup.addTable(freeTable)
            freeTable.setStatus("Prisla skupina ${customerGroup.type.count()}")
            if (rCore.freeWaiters.size > 0) {
                val waiter = rCore.freeWaiters.poll()

                if (!simulationCore.isCooling || simulationCore.cTime < simulationCore.maxTime) {
                    rCore.stats.freeWaitersWeight.addValue(time, rCore.freeWaiters.size)
                }

                C.message("     !!!TABLE STATUS ${rCore.tableManager.getTablesStatus()}")
                rCore.planEvent(BeginOrderEvent(time, customerGroup, waiter))
            } else {
                customerGroup.averageWaiting.startTrack(time, WaitType.FORSERVICE)
                rCore.fifoService.add(customerGroup)
            }
        }
    }

    override fun debugPrint() {
        C.message("ARRIVAL Customer(id: ${customerGroup.getID()}, count: ${customerGroup.type.count()}) TIME: ${time}")
    }

    override fun calendarDescription(): String {
        return "Príchod skupiny: (id: ${customerGroup.getID()}, počet: ${customerGroup.type.count()})"
    }

}