import isel.leic.utils.Time
import kotlin.system.exitProcess

/**On going**/
object TicketMachineApp {
    private const val ORIGIN_STATION = 0
    private const val WAIT_KEY_MS = 2000L

    private fun maintenanceMode() {
        val options = listOf(
            "#-Print Ticket",
            "A-Station Cnt.",
            "B-Coins Cnt.",
            "C-Reset Cnt.",
            "D-Shutdown"
        )
        var optIdx = 0

        fun printTicket_M() {
            var idx = 0
            var roundTrip = true
            val stations = Stations.stations
            if (stations.isEmpty()) return
            while (M.enabled()) {
                val price = stations[idx].price * if (roundTrip) 2 else 1
                TUI.printTicket(stations[idx].station, idx, price / 100.0)
                when (val key = KBD.waitKey(WAIT_KEY_MS)) {
                    'A' -> idx = if (idx > 0) idx - 1 else stations.size - 1
                    'B' -> idx = if (idx < stations.size - 1) idx + 1 else 0
                    'C' -> roundTrip = !roundTrip
                    '*' -> {
                        val st = stations[idx]
                        TUI.toPrint(st.station, roundTrip)
                        if (KBD.waitKey(7000L) != '*') {
                            TUI.vendingAborted()
                            Time.sleep(1200)
                            continue
                        }
                        TUI.processing(st.station)
                        TicketDispenser.activatePrintingTicket(roundTrip, ORIGIN_STATION, idx)
                        TUI.collectTicket(st.station)
                        val start = System.currentTimeMillis()
                        while (!HAL.isBit(INPUTPORTS.FN.mask)
                            && System.currentTimeMillis() - start < 10000L) {
                            Time.sleep(50)
                        }
                        TUI.collectFinished()
                        Time.sleep(1500)
                        return
                    }
                    '#' -> {
                        TUI.vendingAborted()
                        Time.sleep(1200)
                        return
                    }
                    KBD.none -> continue
                    in '0'..'9' -> {
                        val digit = key.digitToInt()
                        if (digit < stations.size) idx = digit
                    }
                }
            }
        }
        fun stationCnt() {
            var idx = 0
            val stations = Stations.stations
            if (stations.isEmpty()) return
            while (M.enabled()) {
                TUI.stationCount(stations[idx].station, idx, stations[idx].sold)
                when (val key = KBD.waitKey(WAIT_KEY_MS)) {
                    'A' -> idx = if (idx > 0) idx - 1 else stations.size - 1
                    'B' -> idx = if (idx < stations.size - 1) idx + 1 else 0
                    '#' -> return
                    KBD.none -> continue
                    in '0'..'9' -> {
                        val digit = key.digitToInt()
                        if (digit < stations.size) idx = digit
                    }
                }
            }
        }
        fun coinsCnt() {
            val values = intArrayOf(5, 10, 20, 50, 100, 200)
            var idx = 0
            while (M.enabled()) {
                TUI.printCoins(values[idx] / 100.0, CoinDeposit.getCount(idx), idx)
                when (val key = KBD.waitKey(WAIT_KEY_MS)) {
                    'A' -> idx = if (idx > 0) idx - 1 else values.size - 1
                    'B' -> idx = if (idx < values.size - 1) idx + 1 else 0
                    '#' -> return
                    KBD.none -> return
                    in '0'..'9' -> {
                        val digit = key.digitToInt()
                        if (digit in values.indices) idx = digit
                    }
                }
            }
        }
        fun resetCounters() {
            if (TUI.yesOrNo("Reset Counters?", 7000)) {
                TUI.write("Resetting...", 0, 0, true, true)
                Stations.reset()
                CoinDeposit.resetCnt()
                Time.sleep(2000)
                TUI.write("Done!", 0, 0, true, true)
                Time.sleep(5000)
            }
        }
        fun shutDown() {
            Stations.save()
            CoinDeposit.saveCoins()
            TUI.write("Shutting down...", 0, 0, true, true)
            Time.sleep(1000)
            exitProcess(0)
        }
        while (M.enabled()) {
            TUI.maintenanceScreen(options[optIdx])
            when (KBD.waitKey(WAIT_KEY_MS)) {
                '#'      -> printTicket_M()
                'A'      -> stationCnt()
                'B'      -> coinsCnt()
                'C'      -> resetCounters()
                'D'      -> if (TUI.yesOrNo("Shutdown?", 5000L)) shutDown()
                KBD.none -> optIdx = (optIdx + 1) % options.size
            }
        }
    }
    private fun normalMode() {
        TUI.startMenu()
        Time.sleep(2000)
        TUI.write("Yet to implement", 0, 0, true, true)
        TUI.write("Try M Mode", 1, 0, true, false)
        Time.sleep(2000)
    }
    fun init() {
        M.init()
        CoinAcceptor.init()
        TicketDispenser.init()
        TUI.init()
        CoinDeposit.init()
        Stations.init()
    }
    fun program() {
        while (true) {
            Time.sleep(10)
            if (M.enabled()) maintenanceMode() else normalMode()
        }
    }
}


fun main() {
    TicketMachineApp.init()
    TicketMachineApp.program()
}
