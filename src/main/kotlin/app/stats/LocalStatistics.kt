package app.stats

import app.model.StatEntry

class LocalStatistics {
    private var entries = mutableListOf<StatEntry>()

    fun getEntries(): MutableList<StatEntry> {
        return entries
    }

    fun update(stats: Statistics, currentTime: Double) {
        entries.clear()

        val customersArrival = stats.getAverageArrivalStats()
        entries.addAll(listOf(
                makeStatEntry(
                        "Aktuálna replikácia",
                        "${stats.getCurrentReplication()}"),
                makeStatEntry(
                        "Denná prevádzka čas",
                        formatStatisticWithTime(currentTime)),
                makeStatEntry(
                        "Priemerný čas čakania",
                        "${stats.waitingTimeAll / stats.customersFinishEating.toDouble()}"),
//                makeStatEntry(
//                        "Priemerný čas čakania - obsluha",
//                        "${stats.getTimeCustomersWait(AverageWaitingType.SERVICE)}"),
//                makeStatEntry(
//                        "Priemerný čas čakania - jedlo",
//                        "${stats.getTimeCustomersWait(AverageWaitingType.MEAL)}"),
//                makeStatEntry (
//                        "Priemerný čas čakania - platba",
//                        "${stats.getTimeCustomersWait(AverageWaitingType.PAY)}"),
                makeStatEntry(
                        "Počet príchodov",
                        "${stats.customersSums}"),
                makeStatEntry(
                        "Počet odchodov",
                        "${stats.leavedCustomers}"),
                makeStatEntry(
                        "Percentuálny počet odchodov",
                        "${if(stats.leavedCustomers == 0) 0 else (stats.leavedCustomers.toDouble() / stats.customersSums.toDouble()) * 100}"),
                makeStatEntry(
                        "Počet voľných čašníkov",
                        "${stats.freeWaitersWeight.getResult()}"),
                makeStatEntry(
                        "Počet voľných kuchárov",
                        "${stats.freeChefssWeight.getResult()}"),
                makeStatEntry(
                        "Priemerný počet voľných stolov(2)",
                        "${stats.freeTableTwoWeight.getResult()}"),
                makeStatEntry(
                        "Priemerný počet voľných stolov(4)",
                        "${stats.freeTableFourWeight.getResult()}"),
                makeStatEntry(
                        "Priemerný počet voľných stolov(6)",
                        "${stats.freeTableSixWeight.getResult()}")
        ))



        val cRep = stats.getWorkingTimes()

        var waitersTime = 0.0
        var chefTime = 0.0
        cRep.first.forEach {
            waitersTime += it.value / stats.simulationTimeDuration
        }
        cRep.second.forEach {
            chefTime += it.value / stats.simulationTimeDuration
        }

        entries.add(makeStatEntry("Voľný čas čašníkov ", "${waitersTime / cRep.first.size * 100} %"))

        stats.getWorkingTimes().first.forEach {
            entries.add(makeStatEntry("Čašník ${it.key} má voľno: ", "${it.value / stats.simulationTimeDuration * 100} %"))
        }

        entries.add(makeStatEntry("Voľno kuchárov ", "${chefTime / cRep.second.size * 100} %"))

        stats.getWorkingTimes().second.forEach {
            entries.add(makeStatEntry("Kuchár ${it.key} ma v priemere voľno: ", "${it.value / stats.simulationTimeDuration * 100} "))
        }
    }

    private fun makeStatEntry(id: String, value: String): StatEntry {
        val statEntry = StatEntry()
        statEntry.id = id
        statEntry.value = value

        return statEntry
    }


    private fun formatStatisticWithTime(data: Double): String {
        return "${C.timeFormatterInc(data)}"
    }

}