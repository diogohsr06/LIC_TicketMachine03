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
    /**
     * Function: createReader()
     *
     * Description: Creates a file reader
     * @param fileName Name of the file to read
     * @return BufferedReader(FileReader(fileName))
     * @see BufferedReader
     */
    private fun createReader(fileName: String): BufferedReader {
        return BufferedReader(FileReader(fileName))
    }
    /**
     * Function: createWriter()
     *
     * Description: Creates a file writer
     * @param fileName Name of the file to read
     * @return PrintWriter(fileName)
     * @see PrintWriter
     */
    private fun createWriter(fileName: String): PrintWriter {
        return PrintWriter(fileName)
    }
    /**
     * Function: toStations()
     *
     * Description: Converts a text line into a Stations object
     * @param line Text line in the format "price;sold;station"
     * @return Stations object
     * @see Stations
     */
    fun toStations(line: String): Stations {
        val parts = line.split(";")
        return Stations(
            parts[0].toInt(),
            parts[1].toInt(),
            parts[2]
        )
    }
    /**
     * Function: toCoins()
     *
     * Description: Converts a text line into a Coins object
     * @param line Text line in the format "value;amount"
     * @return Coins object
     * @see Coins
     */
    fun toCoins(line: String): Coins {
        val parts = line.split(";")
        return Coins(
            parts[0].toInt(),
            parts[1].toInt()
        )
    }
    /**
     * Function: readStations()
     *
     * Description: Reads all stations from a file and stores them in an array
     * @param fileName Name of the file to read
     * @return Array of Stations
     * @see Stations
     */
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
    /**
     * Function: readCoins()
     *
     * Description: Reads all coins from a file and stores them in an array
     * @param fileName Name of the file to read
     * @return Array of Coins
     * @see Coins
     */
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
    /**
     * Function: writeStations()
     *
     * Description: Writes an array of stations to a file
     * @param fileName Name of the file to write
     * @param stations Array of Stations to be written
     * @return void
     * @see Stations
     */
    fun writeStations(fileName: String, stations: Array<Stations>) {
        val writeFile = createWriter(fileName)
        for (i in stations) {
            writeFile.println("${i.price};${i.sold};${i.station}")
        }
        writeFile.close()
    }
    /**
     * Function: writeCoins()
     *
     * Description: Writes an array of coins to a file
     * @param fileName Name of the file to write
     * @param coins Array of Coins to be written
     * @return void
     * @see Coins
     */
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
    print("■ Initializing⬝")
    Time.sleep(1000)
    print("⬝")
    Time.sleep(1000)
    print("⬝\n")
    Time.sleep(1000)
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