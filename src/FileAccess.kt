import java.io.BufferedReader
import java.io.FileReader
import java.io.PrintWriter

/**File writer & File reader**/
object FileAccess {

    data class Stations(val price: Int, val sold: Int, val station: String)
    data class Coins(val value: Int, val amount: Int)

    private fun createReader(fileName: String): BufferedReader {
        return BufferedReader(FileReader(fileName))
    }
    private fun createWriter(fileName: String?): PrintWriter {
        return PrintWriter(fileName)
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
    fun writeStations(fileName: String, stations: Array<Stations>) {
        val writeFile = createWriter(fileName)
        for (i in stations) {
            writeFile.println("${i.price};${i.sold};${i.station}")
        }
        writeFile.close()
    }
    fun writeCoins(fileName: String, coins: Array<Coins>) {
        val writeFile = createWriter(fileName)
        for (i in coins) {
            writeFile.println("${i.value};${i.amount}")
        }
        writeFile.close()
    }
}

/**Teste (Debug)**/
fun main() {
    val a = arrayOf(
        FileAccess.Stations(150, 10, "Rossio"),
        FileAccess.Stations(220, 5, "Oriente"),
        FileAccess.Stations(150, 2, "Santa Apolonia"),
        FileAccess.Stations(350, 0, "Sintra")
    )
    val b = arrayOf(
        FileAccess.Coins(5, 50),
        FileAccess.Coins(10, 20),
        FileAccess.Coins(20, 30),
        FileAccess.Coins(50, 10),
        FileAccess.Coins(100, 15),
        FileAccess.Coins(200, 5)
    )
    FileAccess.readStations("stations.txt")
    FileAccess.readCoins("CoinDeposit.txt")
    FileAccess.writeStations("testStations.txt", a)
    FileAccess.writeCoins("testCoins.txt", b)
}