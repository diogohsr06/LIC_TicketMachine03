import isel.leic.utils.Time

/**Stations**/
object Stations {
    var stations = arrayOf<FileAccess.Stations>()
    fun init() {
        stations = FileAccess.readStations("stations.txt")
    }
    fun sold(station: String) {
        for (i in stations) {
            if (i.station == station) {
                i.sold++
            }
        }
    }
    fun save() {
        FileAccess.writeStations("stations.txt", stations)
    }
    fun reset() {
        for (i in stations) {
            i.sold = 0
        }
    }
}

/**Teste**/
fun main() {
    Stations.init()
    println("Initializing")
    Time.sleep(10000)
    Stations.sold("Madrid")
    println("Madrid ticket sold")
    Time.sleep(10000)
    Stations.save()
    println("Saved in file. Go check")
    Time.sleep(10000)
    Stations.reset()
    println("Sold tickets reseted")
    Time.sleep(10000)
    Stations.save()
    println("Saved in file. Go check")
}