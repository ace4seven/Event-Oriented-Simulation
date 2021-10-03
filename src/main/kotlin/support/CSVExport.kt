package support

import java.io.FileWriter
import java.io.IOException

data class CSVentry(val numbOfCooks: Int,val  numbOfWaiters: Int, val averageWaiting: Double, val averageLeave: Double)

class CSVExport(val fileName: String) {
    var fileWriter: FileWriter? = null
    val delimeter = ','
    val header = "Počet čašníkov, Počet kuchárov, Priemerná doba čakania, Priemerný počet odchodov"

    private var entries =  mutableListOf<CSVentry>()

    fun addRow(entry: CSVentry) {
        entries.add(entry)
    }

    fun generateCSV() {
        try {
            fileWriter = FileWriter(fileName)
            fileWriter?.append(header)
            fileWriter?.append('\n')
        } catch (error: IOException) {
            println(error.message)
        }

        val sEntries = entries.sortedWith(compareBy({ it.averageWaiting }, { it.averageLeave }))
        sEntries.forEach {
            fileWriter?.append("${it.numbOfWaiters} ${delimeter} ${it.numbOfCooks} ${delimeter} ${it.averageWaiting} ${delimeter} ${it.averageLeave}")
            fileWriter?.append('\n')
        }
        close()
    }

    private fun close() {
        try {
            fileWriter?.flush()
            fileWriter?.close()
        } catch (e: IOException) {
            println("Flushing/closing error!")
            e.printStackTrace()
        }
    }

}