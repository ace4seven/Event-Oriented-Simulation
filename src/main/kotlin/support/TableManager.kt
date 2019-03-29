package support

import app.model.CustomerGroupType

enum class TableType {
    TWO, FOUR, SIX;

    fun desc(): String {
        when (this) {
            TWO -> return "Stol pre dvoch"
            FOUR -> return "Stol pre štyroch"
            SIX -> return "Stol pre šiestych"
        }
    }
}

data class RestaurantTable(val id: Int, val type: TableType) {

}

class TableManager {

    private val twoPersonTablesSum = 10
    private val fourPersonTablesSum = 7
    private val sixPersonTablesSum = 6

    private var twoTablesQueue = Queue<RestaurantTable>()
    private var fourTablesQueue = Queue<RestaurantTable>()
    private var sixTablesQueue = Queue<RestaurantTable>()

    init {
        prepareTables()
    }

    private fun prepareTables() {
        for (i in 1..twoPersonTablesSum) { twoTablesQueue.add(RestaurantTable(i, TableType.TWO)) }
        for (i in 1..fourPersonTablesSum) { fourTablesQueue.add(RestaurantTable(i, TableType.FOUR)) }
        for (i in 1..sixPersonTablesSum) { sixTablesQueue.add(RestaurantTable(i, TableType.SIX)) }
    }

    fun clear() {
        twoTablesQueue.clear()
        fourTablesQueue.clear()
        sixTablesQueue.clear()

        prepareTables()
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