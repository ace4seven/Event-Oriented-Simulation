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

    fun desc(): String {
        when(this) {
            ONE -> return  "Skupina zákazníkov 1"
            TWO -> return  "Skupina zákazníkov 2"
            THREE -> return "Skupina zákazníkov 3"
            FOUR -> return "Skupina zákazníkov 4"
            FIVE -> return "Skupina zákazníkov 5"
            SIX -> return "Skupina zákazníkov 6"
        }
    }

}

class CustomerGroup(val type: CustomerGroupType) {

    private var tableType: TableType? = null
    private var finishedMeals: Int = 0

    fun addTable(type: TableType) {
        this.tableType = type
    }

    fun table(): TableType {
        return tableType!!
    }

    fun incMeals() {
        finishedMeals += 1
    }

    fun isReadyForDeploy(): Boolean {
        return finishedMeals == type.count()
    }

}