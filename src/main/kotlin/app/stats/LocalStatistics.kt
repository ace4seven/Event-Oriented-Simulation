package app.stats

import app.model.StatEntry
import core.ConfidenceInterval

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
                        "${stats.getTimeCustomersWait()}"),
                makeStatEntry(
                        "Priemerný čas čakania - obsluha",
                        "${stats.getTimeCustomersWait(AverageWaitingType.SERVICE)}"),
                makeStatEntry(
                        "Priemerný čas čakania - jedlo",
                        "${stats.getTimeCustomersWait(AverageWaitingType.MEAL)}"),
                makeStatEntry (
                        "Priemerný čas čakania - platba",
                        "${stats.getTimeCustomersWait(AverageWaitingType.PAY)}"),
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
                        "${stats.freeWaitersWeight.getWeight()}"),
                makeStatEntry(
                        "Počet voľných kuchárov",
                        "${stats.freeChefssWeight.getWeight()}"),
                makeStatEntry(
                        "Priemerný počet voľných stolov(2)",
                        "${stats.freeTableTwoWeight.getWeight()}"),
                makeStatEntry(
                        "Priemerný počet voľných stolov(4)",
                        "${stats.freeTableFourWeight.getWeight()}"),
                makeStatEntry(
                        "Priemerný počet voľných stolov(6)",
                        "${stats.freeTableSixWeight.getWeight()}")
        ))



        val cRep = stats.getWorkingTimes()

        var waitersTime = 0.0
        var chefTime = 0.0
        cRep.first.forEach {
            waitersTime += it.value
        }
        cRep.second.forEach {
            chefTime += it.value
        }

        entries.add(makeStatEntry("Priemerne voľno čašníkov ", "${waitersTime / cRep.first.size * 100} %"))

        stats.getWorkingTimes().first.forEach {
            entries.add(makeStatEntry("Čašník ${it.key} ma v priemere voľno: ", "${it.value * 100} %"))
        }

        entries.add(makeStatEntry("Priemerne voľno kuchárov ", "${chefTime / cRep.second.size * 100} %"))

        stats.getWorkingTimes().second.forEach {
            entries.add(makeStatEntry("Kuchár ${it.key} ma v priemere voľno: ", "${it.value  * 100} %"))
        }
    }

    private fun makeStatEntry(id: String, value: String): StatEntry {
        val statEntry = StatEntry()
        statEntry.id = id
        statEntry.value = value

        return statEntry
    }


    private fun formatStatisticWithTime(data: Double): String {
        return "${C.timeFormatter(data)}"
    }

}