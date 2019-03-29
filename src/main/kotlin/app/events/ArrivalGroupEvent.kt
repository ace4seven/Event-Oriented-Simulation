package app.events
import app.model.CustomerGroup
import app.model.CustomerGroupType
import core.Event
import core.EventSimulationCore
import core.RestaurantSimulationCore

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

        rCore.planEvent(ArrivalGroupEvent(nextCome + simulationCore.cTime, CustomerGroup(customerGroup.type)))
        C.message("Plán skupiny: ${customerGroup.type.desc()} v s.č: ${time}")

        val freeTable = rCore.tableManager.findedTable(customerGroup.type)

        if (freeTable == null) {
            C.message("Odcháza z dôvodu plnej kapacity stolov: ${customerGroup.type.desc()}")
        } else {
            customerGroup.addTable(freeTable)
            C.message("Zaradeny stôl: ${freeTable.desc()}")
            if (rCore.freeWaiters.size > 0) {
                rCore.planEvent(BeginOrderEvent(rCore.cTime, customerGroup))
                C.message("Začiatok objednávky pre skupinu: ${customerGroup.type.desc()}")
            } else {
                rCore.fifoService.add(customerGroup)
                C.message("${customerGroup.type.desc()} zaradená do FIFO čakania na objednávku")
            }
        }
    }

}