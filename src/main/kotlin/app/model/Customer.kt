package app.model

import support.TableType

enum class CustomerGroupType {
    ONE, TWO, THREE, FOUR, FIVE, SIX;

    fun count(): Int {
        when(this) {
            ONE -> return  1
            TWO -> return  2
            THREE -> return 3
            FOUR -> return 4
            FIVE -> return 5
            SIX -> return 6
        }
    }

}

class Customer(val type: CustomerGroupType) {

    private var tableType: TableType? = null

    fun addTable(type: TableType) {
        this.tableType = type
    }
}