package app.model

abstract class Worker(id: Int): Person(id) {

    private var workingTime = 0.0

    private var startWorkingTime = 0.0
    private var stopWorkingTime = 0.0

    private var planToStopWorkingTime = 0.0

    private var status = ""
    private var isWorking = false

    fun startWorking(time: Double, planToStop: Double) {
        isWorking = true
        startWorkingTime = time
        planToStopWorkingTime = planToStop
    }

    fun stopWorking(time: Double) {
        isWorking = false
        stopWorkingTime = time
        workingTime += (stopWorkingTime - startWorkingTime)

        if (workingTime < 0) { throw Exception("Working time have to be positive number") }
    }

    fun getWorkingTime(): Double {
        return workingTime
    }

    fun getCompletion(currentTime: Double): String {
        if (isWorking) {
            return "${((currentTime - startWorkingTime) / (planToStopWorkingTime - startWorkingTime) * 100).toInt()} %"
        } else { return "-" }
    }

    fun addStatus(message: String) {
        status = message
    }

    fun getStatus(): String {
        if (isWorking) {
            return status
        } else {
            return "Čaká na prácu"
        }
    }

}