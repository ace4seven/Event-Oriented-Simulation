package app.stats

class Statistics {

    var count = 0;
    var time = 0.0
    var replication = 0
    var repResult = 0.0

    fun incResult() {
        repResult +=  time / count.toDouble()
        time = 0.0
        count = 0
    }
}