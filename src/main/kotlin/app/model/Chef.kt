package app.model

class Chef: Comparable<Waiter>, Worker() {

    override fun compareTo(other: Waiter): Int {
        if (getNoWorkingTime() == other.getNoWorkingTime()) { return 0 }
        if (getNoWorkingTime() < other.getNoWorkingTime()) { return -1 }

        return 1
    }

}