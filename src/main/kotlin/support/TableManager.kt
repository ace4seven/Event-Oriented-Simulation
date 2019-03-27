package support

import app.model.CustomerGroupType

enum class TableType {
    TWO, FOUR, SIX
}

class TableManager {

    private var twoPersonTables = 10
    private var fourPersonTables = 7
    private var sixPersonTables = 6

    fun findedTable(group: CustomerGroupType): TableType? {
        when(group) {
            CustomerGroupType.ONE -> return manageTables(1)
            CustomerGroupType.TWO -> return manageTables(2)
            CustomerGroupType.THREE -> return manageTables(3)
            CustomerGroupType.FOUR -> return manageTables(4)
            CustomerGroupType.FIVE -> return manageTables(5)
            CustomerGroupType.SIX -> return manageTables(6)

        }
    }

    fun freeTable(type: TableType) {
        when(type) {
            TableType.TWO -> twoPersonTables += 1
            TableType.FOUR -> fourPersonTables += 1
            TableType.SIX -> sixPersonTables += 1
        }
    }

    private fun manageTables(persons: Int): TableType? {
        if (twoPersonTables > 0 && persons < 3) {
            twoPersonTables -= 1
            return TableType.TWO
        } else if (fourPersonTables > 0 && persons < 5) {
            fourPersonTables -= 1
            return TableType.FOUR
        } else if (sixPersonTables > 0 && persons < 7) {
            sixPersonTables -= 1
            return TableType.SIX
        }

        return null
    }

}