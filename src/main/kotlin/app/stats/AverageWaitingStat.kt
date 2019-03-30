package app.stats

enum class WaitType {
    FORSERVICE, FORMEAL, FORPAY
}

data class WaitingEntry(val type: WaitType, var start: Double = 0.0, var result: Double = 0.0)

class AverageWaitingStat {

    private var serviceWaitingTime = WaitingEntry(WaitType.FORSERVICE)
    private var mealWaitingTime = WaitingEntry(WaitType.FORMEAL)
    private var payWaitingTime = WaitingEntry(WaitType.FORPAY)

    private var canStart = true

    fun startTrack(cTime: Double, type: WaitType) {
        if (!canStart) {
            throw Exception("Cannot start multiple times")
        }

        when(type) {
            WaitType.FORSERVICE -> serviceWaitingTime.start = cTime
            WaitType.FORMEAL -> mealWaitingTime.start = cTime
            WaitType.FORPAY -> payWaitingTime.start = cTime
        }

        canStart = false
    }

    fun stopTrack(cTime: Double, type: WaitType) {
        if (canStart) {
            throw Exception("Cannot start multiple times")
        }

        when(type) {
            WaitType.FORSERVICE -> serviceWaitingTime.result = (cTime - serviceWaitingTime.start)
            WaitType.FORMEAL -> mealWaitingTime.result = (cTime - mealWaitingTime.start)
            WaitType.FORPAY -> payWaitingTime.result = (cTime - payWaitingTime.start)
        }

        canStart = true
    }

    fun getResult(type: WaitType): WaitingEntry {
        when(type) {
            WaitType.FORSERVICE -> return serviceWaitingTime
            WaitType.FORMEAL -> return mealWaitingTime
            WaitType.FORPAY -> return payWaitingTime
        }
    }

}