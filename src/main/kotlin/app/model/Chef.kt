package app.model

class Chef(id: Int): Comparable<Chef>, Worker(id) {

    override fun compareTo(other: Chef): Int {
        if (getWorkingTime() == other.getWorkingTime()) { return 0 }
        if (getWorkingTime() < other.getWorkingTime()) { return -1 }

        return 1
    }

}