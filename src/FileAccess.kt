import isel.leic.utils.Time
import java.io.BufferedReader
import java.io.FileReader
import java.io.PrintWriter

//======================================================================================================================
//                                                    File Access
//======================================================================================================================
object FileAccess {
    /**Data structures**/
    data class Stations(val price: Int, var sold: Int, val station: String)
    data class Coins(val value: Int, var amount: Int)
    /**Creates a file reader**/
    private fun createReader(fileName: String): BufferedReader {
        return BufferedReader(FileReader(fileName))
    }
    /**Creates a file writer**/
    private fun createWriter(fileName: String): PrintWriter {
        return PrintWriter(fileName)
    }
    /**Converts string to Stations**/
    fun toStations(line: String): Stations {
        val parts = line.split(";")
        return Stations(
            parts[0].toInt(),
            parts[1].toInt(),
            parts[2]
        )
    }
    /**Converts string to Coins**/
    fun toCoins(line: String): Coins {
        val parts = line.split(";")
        return Coins(
            parts[0].toInt(),
            parts[1].toInt()
        )
    }
    /**Extracts file content, splits and converts to Array (Stations)**/
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
    /**Extracts file content, splits and converts to Array (Coins)**/
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
    /**Converts back and writes on the file (Stations)**/
    fun writeStations(fileName: String, stations: Array<Stations>) {
        val writeFile = createWriter(fileName)
        for (i in stations) {
            writeFile.println("${i.price};${i.sold};${i.station}")
        }
        writeFile.close()
    }
    /**Converts back and writes on the file (Coins)**/
    fun writeCoins(fileName: String, coins: Array<Coins>) {
        val writeFile = createWriter(fileName)
        for (i in coins) {
            writeFile.println("${i.value};${i.amount}")
        }
        writeFile.close()
    }
}

//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    println("■ File Writer & File Reader")
    println("■ Initializing...")
    Time.sleep(3000)
    println("=============================================================================")
    while (true) {
        println("■ Choose a task: ")
        println("1 - Write predefined Arrays")
        println("2 - Read existing files")
        println("3 - Write your own Data")
        println("D  - Quit")
        print("> ")
        when (readln().uppercase()) {
            "1" -> {
                val a = arrayOf(
                    FileAccess.Stations(150, 10, "Morangos com açucar"),
                    FileAccess.Stations(220, 5, "Matrix"),
                    FileAccess.Stations(100, 3, "Dune"))
                val b = arrayOf(
                    FileAccess.Coins(5, 50),
                    FileAccess.Coins(10, 20),
                    FileAccess.Coins(20, 30),
                    FileAccess.Coins(50, 10),
                    FileAccess.Coins(100, 15),
                    FileAccess.Coins(200, 5))
                FileAccess.writeStations("testStations.txt", a)
                FileAccess.writeCoins("testCoins.txt", b)
                println("■ Data written successfully! Check test files.")
            }
            "2" -> {
                val s = FileAccess.readStations("testStations.txt")
                val c = FileAccess.readCoins("testCoins.txt")
                s.forEach { println("Station: ${it.station}, Price: ${it.price}, Sold: ${it.sold}") }
                c.forEach { println("Coin: ${it.value}, Amount: ${it.amount}") }
            }
            "3" -> {
                println("■ Manual data Insertion")
                val stations = mutableListOf<FileAccess.Stations>()
                println("■ Insert Stations (format: price;sold;station) or press Q to finish")
                while (true) {
                    print("Station: ")
                    val input = readln()
                    if (input.uppercase() == "Q") break
                    try { stations.add(FileAccess.toStations(input)) } catch (e: Exception) { println("■ Invalid Format! Use: price;sold;station") }
                }
                val coins = mutableListOf<FileAccess.Coins>()
                println("■ Insert Coins (format: value;amount) or press Q to finish")
                while (true) {
                    print("Coin: ")
                    val input = readln()
                    if (input.uppercase() == "Q") break
                    try { coins.add(FileAccess.toCoins(input)) } catch (e: Exception) { println("■ Invalid format! Use: value;amount") }
                }
                if (stations.isNotEmpty()) FileAccess.writeStations("testStations.txt", stations.toTypedArray())
                if (coins.isNotEmpty()) FileAccess.writeCoins("testCoins.txt", coins.toTypedArray())
                println("■ Data written successfully! Check test files.")
            }
            "D" -> break
            else -> println("■ Invalid option, try again.")
        }
    }
}