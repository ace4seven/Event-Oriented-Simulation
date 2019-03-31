package app.model

import javafx.beans.property.Property
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class StatEntry {
    var id: String by property<String>()
    var value: String by property<String>()
}