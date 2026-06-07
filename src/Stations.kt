import isel.leic.utils.Time

//======================================================================================================================
//                                                      Stations
//======================================================================================================================
object Stations {
    var stations = arrayOf<FileAccess.Stations>()
    /**Inits the object**/
    fun init() {
        stations = FileAccess.readStations("stations.txt")
    }
    /**Increments sold whenever a ticket is sold**/
    fun sold(station: String) {
        for (i in stations) {
            if (i.station == station) {
                i.sold++
            }
        }
    }
    /**Saves on file**/
    fun save() {
        FileAccess.writeStations("stations.txt", stations)
    }
    /**Resets counters**/
    fun reset() {
        for (i in stations) {
            i.sold = 0
        }
    }
}

//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    Stations.init()
    println("■ Initializing...")
    Time.sleep(3000)
    println("=============================================================================")
    println("■ Avalable Stations:")
    Stations.stations.forEach { println("- ${it.station} (Price: ${it.price})") }
    println("======================================")
    print("■ Insert station for testing: ")
    val s = readln()
    println("\n■ Station '$s' selected.")
    println("■ Choose a task:")
    println("0 - Sell a ticket")
    println("1 - Reset counters")
    println("2 - Save")
    println("3 - Quit")
    println("======================================")
    var exit = false
    while (!exit) {
        val station = Stations.stations.find { it.station == s }
        if (station == null) {
            print("■ Error: Station '$s' not found!")
            break
        }
        print("\r■ Station: ${station.station} | Sold: ${station.sold}\n")
        print("\r> ")
        val key = readln().toInt()
        when (key) {
            0 -> {
                Stations.sold(s)
                println("■ Ticket for $s sold!")
            }
            1 -> {
                Stations.reset()
                println("■ Counters have been reset!")
            }
            2 -> {
                Stations.save()
                println("■ Saved!")
            }
            3 -> {
                println("■ Quitting...")
                exit = true
            }
        }
        Time.sleep(100)
    }
}
