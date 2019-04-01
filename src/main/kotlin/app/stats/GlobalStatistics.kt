package app.stats

import app.model.StatEntry
import core.ConfidenceInterval

class GlobalStatistics {
    private var entries = mutableListOf<StatEntry>()

    fun getEntries(): MutableList<StatEntry> {
        return entries
    }

    fun update(stats: Statistics) {
        entries.clear()

        val customersArrival = stats.getAverageArrivalStats()
        entries.addAll(listOf(
                makeStatEntry(
                        "Aktuálna replikácia",
                        "${stats.getCurrentReplication()}"),
                makeStatEntry(
                        "Denná prevádzka čas",
                        formatStatisticWithTime(stats.getAverageBusinessTime())),
                makeStatEntry(
                        "Priemerný čas čakania",
                        formatStatisticWaitingTime(stats.getAverageWaitingTime())),
//                makeStatEntry(
//                        "Priemerný čas čakania - obsluha",
//                        formatStatistic(stats.getAverageTimeCustomerWait(AverageWaitingType.SERVICE))),
//                makeStatEntry(
//                        "Priemerný čas čakania - jedlo",
//                        formatStatistic(stats.getAverageTimeCustomerWait(AverageWaitingType.MEAL))),
//                makeStatEntry (
//                        "Priemerný čas čakania - platba",
//                        formatStatistic(stats.getAverageTimeCustomerWait(AverageWaitingType.PAY))),
                makeStatEntry(
                        "Priemerný počet príchodov za deň",
                        "${customersArrival.first}"),
                makeStatEntry(
                        "Priemerný počet odchodov za deň",
                        "${customersArrival.second}"),
                makeStatEntry(
                        "Priemerný percentuálny počet odchodov",
                        formatStatistic(customersArrival.third)),
                makeStatEntry(
                        "Priemerný počet voľných čašníkov",
                        formatStatistic(stats.getAverageHeight(HeightType.WAITER))),
                makeStatEntry(
                        "Priemerný počet voľných kuchárov",
                        formatStatistic(stats.getAverageHeight(HeightType.CHEF))),
                makeStatEntry(
                        "Priemerný počet voľných stolov(2)",
                        formatStatistic(stats.getAverageHeight(HeightType.TABLE_TWO))),
                makeStatEntry(
                        "Priemerný počet voľných stolov(4)",
                        formatStatistic(stats.getAverageHeight(HeightType.TABLE_FOUR))),
                makeStatEntry(
                        "Priemerný počet voľných stolov(6)",
                        formatStatistic(stats.getAverageHeight(HeightType.TABLE_SIX)))
        ))

        val cRep = stats.getAverageWorkingTimes().third
        stats.getAverageWorkingTimes().first.forEach {
            entries.add(makeStatEntry("Čašník ${it.key} ma v priemere voľno: ", "${it.value / cRep * 100} %"))
        }

        stats.getAverageWorkingTimes().second.forEach {
            entries.add(makeStatEntry("Kuchár ${it.key} ma v priemere voľno: ", "${it.value / cRep * 100} %"))
        }
    }

    private fun makeStatEntry(id: String, value: String): StatEntry {
        val statEntry = StatEntry()
        statEntry.id = id
        statEntry.value = value

        return statEntry
    }

    private fun formatStatistic(data: Triple<Double?, Double, Double?>): String {
        return "${data.second} " +
                " - IS: <${if (data.first != null) data.first!! else "NO_DATA"}, " +
                "${if (data.third != null) data.third!! else "NO_DATA"}>"
    }

    private fun formatStatisticWaitingTime(data: ConfidenceInterval): String {
        return "${data.median()} " +
                " - IS: <${if (data.ISLeft() != null) data.ISLeft()!! else "NO_DATA"}, " +
                "${if (data.ISRight() != null) data.ISRight()!! else "NO_DATA"}>"
    }

    private fun formatStatistic(data: ConfidenceInterval): String {
        return "${data.median() * 100} %" +
                " - IS: <${if (data.ISLeft() != null) data.ISLeft()!! * 100 else "NO_DATA"}, " +
                "${if (data.ISRight() != null) data.ISRight()!! * 100 else "NO_DATA"}>"
    }

    private fun formatStatisticWithTime(data: ConfidenceInterval): String {
        return "${C.timeFormatterInc(data.median())} " +
                " - IS: <${if (data.ISLeft() != null) C.timeFormatterInc(data.ISLeft()!!) else "NO_DATA"}, " +
                "${if (data.ISRight() != null) C.timeFormatterInc(data.ISRight()!!) else "NO_DATA"}>"
    }

}