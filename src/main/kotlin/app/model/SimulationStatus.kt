package app.model

import tornadofx.*

class CalendarData {
    var time: String by property<String>()
    var desc: String by property<String>()

    companion object {
        fun make(time: Double, desc: String): CalendarData {
            val data = CalendarData()
            data.desc = desc
            data.time = C.timeFormatterInc(time)
            return data
        }
    }
}

class WorkerData {
    var id: String by property<String>()
    var time: String by property<String>()
    var status: String by property<String>()
    var completion: String by property<String>()

    companion object {
        fun make(id: Int, time: Double, status: String, completion: String): WorkerData {
            val data = WorkerData()
            data.time = C.timeFormatter(time)
            data.id = "${id}"
            data.status = status
            data.completion = completion
            return data
        }
    }
}

class WaitingData {
    var id: String by property<String>()
    var capacity: String by property<String>()
//    var time: String by property<String>()

    companion object {
        fun make(id: Int, capacity: Int): WaitingData {
            val data = WaitingData()
            data.id = "${id}"
            data.capacity = "${capacity}"
//            data.time = C.timeFormatter(time)
            return data
        }
    }
}

class TableData {
    var id: String by property<String>()
    var capacity: String by property<String>()
    var status: String by property<String>()

    companion object {
        fun make(id: Int, capacity: Int, status: String): TableData {
            val data = TableData()
            data.id = "${id}"
            data.capacity = "${capacity}"
            data.status = status
            return data
        }
    }
}

class FreeWorkerData {
    var id: String by property<String>()
    var time: String by property<String>()

    companion object {
        fun make(id: Int, time: Double): FreeWorkerData {
            val data = FreeWorkerData()
            data.id = "${id}"
            data.time = C.timeFormatter(time)
            return data
        }
    }
}

class MealFrontData {
    var name: String by property<String>()
    var tableID: String by property<String>()

    companion object {
        fun make(name: String, tableID: Int): MealFrontData {
            val data = MealFrontData()
            data.name = name
            data.tableID = "${tableID}"
            return data
        }
    }
}