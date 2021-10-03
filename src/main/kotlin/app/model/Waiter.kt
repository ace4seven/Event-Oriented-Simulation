package app.model

class Waiter(id: Int): Comparable<Waiter>, Worker(id) {

    override fun compareTo(other: Waiter): Int {
        if (getWorkingTime() == other.getWorkingTime()) { return 0 }
        if (getWorkingTime() < other.getWorkingTime()) { return -1 }

        return 1
    }

}