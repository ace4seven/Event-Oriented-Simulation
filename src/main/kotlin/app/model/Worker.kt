package app.model

abstract class Worker {

    private var noWorkingTime = 0.0

    private var startWorkTime = 0.0
    private var stopWorking = 0.0

    fun startWorking(time: Double) {
        startWorkTime = time
    }

    fun stopWorking(time: Double) {
        stopWorking = time
    }

    fun getNoWorkingTime(): Double {
        return noWorkingTime
    }

    fun resetTimes() {
        noWorkingTime = 0.0
        startWorkTime = 0.0
        stopWorking = 0.0
    }

}