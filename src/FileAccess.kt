import java.io.File
import java.io.BufferedWriter
import java.io.BufferedReader
import java.io.FileReader
import java.io.PrintWriter
import java.io.FileWriter

/**File writer & File reader**/
object FileAccess {

    data class Stations(val price: Int, val sold: Int, val station: String)
    data class Coins(val value: Int, val amount: Int)

    private fun createReader(fileName: String): BufferedReader {
        return BufferedReader(FileReader(fileName))
    }
    fun toStations(line: String): Stations {
        val parts = line.split(";")
        return Stations(
            parts[0].toInt(),
            parts[1].toInt(),
            parts[2]
        )
    }
    fun toCoins(line: String): Coins {
        val parts = line.split(";")
        return Coins(
            parts[0].toInt(),
            parts[1].toInt()
        )
    }
    fun readStations(fileName: String): Array<Stations> {
        val readFile = createReader(fileName)
        var line = readFile.readLine()
        var stations = mutableListOf<Stations>()
        while (line != null) {
            stations.add(toStations(line))
            line = readFile.readLine()
        }
        return stations.toTypedArray()
    }
    fun readCoins(fileName: String): Array<Coins> {
        val readFile = createReader(fileName)
        var line = readFile.readLine()
        var coins = mutableListOf<Coins>()
        while (line != null) {
            coins.add(toCoins(line))
            line = readFile.readLine()
        }
        return coins.toTypedArray()
    }
}

/**Teste (Debug)**/
fun main() {
    FileAccess.readStations("stations.txt")
    FileAccess.readCoins("CoinDeposit.txt")
}