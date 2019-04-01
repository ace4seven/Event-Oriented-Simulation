package app.stats

enum class WaitType {
    FORSERVICE, FORMEAL, FORPAY
}

data class WaitingEntry(val type: WaitType, var start: Double = 0.0, var result: Double = 0.0)

class AverageWaitingStat {

    private var serviceWaitingTime = WaitingEntry(WaitType.FORSERVICE)
    private var mealWaitingTime = WaitingEntry(WaitType.FORMEAL)
    private var payWaitingTime = WaitingEntry(WaitType.FORPAY)

    var canStopTrack = false
        private set

    fun startTrack(cTime: Double, type: WaitType) {
        if (canStopTrack) {
            throw Exception("Cannot start multiple times")
        }

        when(type) {
            WaitType.FORSERVICE -> serviceWaitingTime.start = cTime
            WaitType.FORMEAL -> mealWaitingTime.start = cTime
            WaitType.FORPAY -> payWaitingTime.start = cTime
        }

        canStopTrack = true
    }

    fun stopTrack(cTime: Double, type: WaitType) {
        if (!canStopTrack) {
            throw Exception("Cannot start multiple times")
        }

        when(type) {
            WaitType.FORSERVICE -> {
                val result = cTime - serviceWaitingTime.start
                if (result < 0) throw Exception("STATISTIC TIME ERROR COUNTING")
                serviceWaitingTime.result = result
            }
            WaitType.FORMEAL -> {
                val result = cTime - mealWaitingTime.start
                if (result < 0) throw Exception("STATISTIC TIME ERROR COUNTING")
                mealWaitingTime.result = result
            }
            WaitType.FORPAY -> {
                val result = cTime - payWaitingTime.start
                if (result < 0) { throw Exception("STATISTIC TIME ERROR COUNTING") }
                payWaitingTime.result = (cTime - payWaitingTime.start)
            }
        }

        canStopTrack = false
    }

    fun getResult(type: WaitType): WaitingEntry {
        when(type) {
            WaitType.FORSERVICE -> return serviceWaitingTime
            WaitType.FORMEAL -> return mealWaitingTime
            WaitType.FORPAY -> return payWaitingTime
        }
    }

    fun getResult(): Double {
        return (serviceWaitingTime.result + mealWaitingTime.result + payWaitingTime.result)
    }

}