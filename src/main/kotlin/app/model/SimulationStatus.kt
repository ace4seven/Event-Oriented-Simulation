package app.model

import tornadofx.*

class CalendarData {
    var time: String by property<String>()
    var desc: String by property<String>()
}

class WorkerData {
    var id: String by property<String>()
    var time: String by property<String>()
    var status: String by property<String>()
    var completion: String by property<String>()
}

class WaitingData {
    var id: String by property<String>()
    var capacity: String by property<String>()
    var time: String by property<String>()
}

class TableData {
    var id: String by property<String>()
    var capacity: String by property<String>()
    var status: String by property<String>()
}

class FreeWorkerData {
    var id: String by property<String>()
    var time: String by property<String>()
}

class MealFrontData {
    var name: String by property<String>()
    var tableID: String by property<String>()
}