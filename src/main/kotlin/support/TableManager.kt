package support

import app.model.CustomerGroupType
import app.stats.StateStatistic

enum class TableType {
    TWO, FOUR, SIX;

    fun desc(): String {
        when (this) {
            TWO -> return "Stol pre dvoch"
            FOUR -> return "Stol pre štyroch"
            SIX -> return "Stol pre šiestych"
        }
    }

    fun count(): Int {
        when (this) {
            TWO -> return 2
            FOUR -> return 4
            SIX -> return 6
        }
    }
}

data class RestaurantTable(val id: Int, val type: TableType) {

    var status: String  = "Voľný"
        private set

    fun setStatus(value: String) {
        this.status = value
    }

}

class TableManager {

    private val twoPersonTablesSum = 10
    private val fourPersonTablesSum = 7
    private val sixPersonTablesSum = 6

    var twoTablesQueue = Queue<RestaurantTable>()
        private set

    var fourTablesQueue = Queue<RestaurantTable>()
        private set

    var sixTablesQueue = Queue<RestaurantTable>()
        private set

    fun getTablesStatus(): String {
        return "Tables(TWO:${twoTablesQueue.size()}, FOUR:${fourTablesQueue.size()}, SIX:${sixTablesQueue.size()})"
    }

    fun prepareTables(states: StateStatistic? = null) {
        var index = 1
        for (i in 1..twoPersonTablesSum) {
            val table = RestaurantTable(index, TableType.TWO)
            states?.subscribeTable(table)
            twoTablesQueue.add(table)
            index += 1
        }
        for (i in 1..fourPersonTablesSum) {
            val table = RestaurantTable(index, TableType.FOUR)
            states?.subscribeTable(table)
            fourTablesQueue.add(table)
            index += 1
        }
        for (i in 1..sixPersonTablesSum) {
            val table = RestaurantTable(index, TableType.SIX)
            states?.subscribeTable(table)
            sixTablesQueue.add(table)
            index += 1
        }
    }

    fun reset(states: StateStatistic? = null) {
        twoTablesQueue.clear()
        fourTablesQueue.clear()
        sixTablesQueue.clear()

        prepareTables(states)
    }

    fun findedTable(group: CustomerGroupType): RestaurantTable? {
        when(group) {
            CustomerGroupType.ONE -> return manageTables(1)
            CustomerGroupType.TWO -> return manageTables(2)
            CustomerGroupType.THREE -> return manageTables(3)
            CustomerGroupType.FOUR -> return manageTables(4)
            CustomerGroupType.FIVE -> return manageTables(5)
            CustomerGroupType.SIX -> return manageTables(6)

        }
    }

    fun freeTable(table: RestaurantTable) {
        when(table.type) {
            TableType.TWO -> twoTablesQueue.add(table)
            TableType.FOUR -> fourTablesQueue.add(table)
            TableType.SIX -> sixTablesQueue.add(table)
        }
    }

    private fun manageTables(persons: Int): RestaurantTable? {
        if (twoTablesQueue.size() > 0 && persons < 3) {
            return twoTablesQueue.pop()
        } else if (fourTablesQueue.size() > 0 && persons < 5) {
            return fourTablesQueue.pop()
        } else if (sixTablesQueue.size() > 0 && persons < 7) {
            return sixTablesQueue.pop()
        }

        return null
    }

}