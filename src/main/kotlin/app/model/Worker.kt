package app.model

abstract class Worker(id: Int): Person(id) {

    private var workingTime = 0.0

    private var startWorkingTime = 0.0
    private var stopWorkingTime = 0.0

    fun startWorking(time: Double) {
        startWorkingTime = time
    }

    fun stopWorking(time: Double) {
        stopWorkingTime = time
        workingTime += (stopWorkingTime - startWorkingTime)

        if (workingTime < 0) { throw Exception("Working time have to be positive number") }
    }

    fun getWorkingTime(): Double {
        return workingTime
    }

}