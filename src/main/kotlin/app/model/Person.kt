package app.model

abstract class Person(val id: Int) {

    fun getID(): Int {
        return this.id
    }

}