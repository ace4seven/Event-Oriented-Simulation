package support

import java.util.*

class Queue<Any> {

    var items:MutableList<Any> = LinkedList<Any>()

    fun isEmpty():Boolean = items.isEmpty()
    fun size():Int = items.count()

    override  fun toString() = items.toString()

    fun add(element:Any){
        items.add(element)
    }

    fun pop():Any?{
        if (this.isEmpty()){
            return null
        } else {
            return items.removeAt(0)
        }
    }

    fun items(): MutableList<Any> {
        return items
    }

    fun peek():Any?{
        return items[0]
    }

    fun clear() {
        items.clear()
    }

}