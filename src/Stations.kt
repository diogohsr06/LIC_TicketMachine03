import isel.leic.utils.Time

//======================================================================================================================
//                                                      Stations
//======================================================================================================================
object Stations {
    var stations = arrayOf<FileAccess.Stations>()
    /**
     * Function: init()
     *
     * Description: This functions inits the object
     * @param void
     * @return void
     * @see FileAccess.readStations
     */
    fun init() {
        stations = FileAccess.readStations("stations.txt")
    }
    /**
     * Function: sold()
     *
     * Description: Increments sold whenever a ticket is sold
     * @param station Destination
     * @return void
     */
    fun sold(station: String) {
        for (i in stations) {
            if (i.station == station) {
                i.sold++
            }
        }
    }
    /**
     * Function: save()
     *
     * Description: Saves on file
     * @param void
     * @return void
     * @see FileAccess.writeStations
     */
    fun save() {
        FileAccess.writeStations("stations.txt", stations)
    }
    /**
     * Function: reset()
     *
     * Description: Resets counters
     * @param void
     * @return void
     */
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
    print("■ Initializing⬝")
    Time.sleep(1000)
    print("⬝")
    Time.sleep(1000)
    print("⬝\n")
    Time.sleep(1000)
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
