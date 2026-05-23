import TUI.vendingAborted
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
                        TUI.toPrint(st.station, !roundTrip)
                        if (KBD.waitKey(7000L) != '*') {
                            TUI.vendingAborted()
                            Thread.sleep(1200)
                            continue
                        }
                        TUI.processing(st.station)
                        Time.sleep(3000)
                        TicketDispenser.activatePrintingTicket(roundTrip, ORIGIN_STATION, idx)
                        TUI.collectTicket(st.station)
                        while (!HAL.isBit(INPUTPORTS.FN.mask)) {
                            Thread.sleep(10)
                        }
                        TicketDispenser.lowerPrt(roundTrip, ORIGIN_STATION, idx)
                        Thread.sleep(200)
                        TUI.collectFinished()
                        Thread.sleep(1500)
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
        val stations = Stations.stations
        if (stations.isEmpty()) return
        fun pollCoin() {
            if (!CoinAcceptor.coinInserted()) return
            val id = CoinAcceptor.getCoinId()
            if (id in 0..5) {
                CoinDeposit.insert(id)
            }
            CoinAcceptor.coinAccept()
        }
        fun abortVending() {
            val returned = CoinDeposit.getTotal()
            if (returned > 0) {
                CoinAcceptor.coinReturn()
                CoinDeposit.cancel()
                TUI.vendingAborted2(returned / 100.0)
            } else {
                TUI.vendingAborted()
            }
            Time.sleep(1200)
        }
        fun waitForPayment(station: String, required: Int): Boolean {
            while (!M.enabled()) {
                pollCoin()
                val inserted = CoinDeposit.getTotal()
                TUI.write(station, 0, 0, true, true)
                TUI.write(
                    "${Others.centsToEuros(inserted)}/${Others.centsToEuros(required)}${0.toChar()}",
                    1,
                    0,
                    true,
                    false
                )
                if (inserted >= required) return true
                if (KBD.waitKey(200) == '#') return false
            }
            return false
        }
        var idx = 0
        var roundTrip = false
        while (!M.enabled()) {
            val st = stations[idx]
            val price = st.price * if (roundTrip) 2 else 1
            pollCoin()
            TUI.printTicket(st.station, idx, price / 100.0)
            when (val key = KBD.waitKey(WAIT_KEY_MS)) {
                'A' -> idx = if (idx > 0) idx - 1 else stations.size - 1
                'B' -> idx = if (idx < stations.size - 1) idx + 1 else 0
                'C' -> roundTrip = !roundTrip
                '*' -> {
                    TUI.toPrint(st.station, !roundTrip)
                    if (KBD.waitKey(7000L) != '*') {
                        abortVending()
                        continue
                    }
                    if (!waitForPayment(st.station, price)) {
                        abortVending()
                        continue
                    }
                    if (!CoinDeposit.exchange(price)) {
                        abortVending()
                        continue
                    }
                    CoinAcceptor.coinCollect()
                    TUI.processing(st.station)
                    Time.sleep(3000)
                    TicketDispenser.activatePrintingTicket(roundTrip, ORIGIN_STATION, idx)
                    TUI.collectTicket(st.station)
                    while (!HAL.isBit(INPUTPORTS.FN.mask)) {
                        Thread.sleep(10)
                    }
                    TicketDispenser.lowerPrt(roundTrip, ORIGIN_STATION, idx)
                    Stations.sold(st.station)
                    Stations.save()
                    TUI.collectFinished()
                    Time.sleep(1500)
                    return
                }
                '#' -> {
                    abortVending()
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
            TUI.startMenu()
            if (M.enabled()) maintenanceMode()
            if (KBD.waitKey(WAIT_KEY_MS) == '#') normalMode()
        }
    }
}

fun main() {
    TicketMachineApp.init()
    println("=======================Navigation in Vending Mode=======================")
    println("# -> Enter vending mode")

    println("Inside:")
    println("C -> Select trip type")
    println("* -> Confirm")
    println("# -> Abort")

    println("=======================Navigation in Maintenance Mode=======================")
    println("# -> Simulate printing ticket")
    println("     C -> Select trip type")
    println("     * -> Confirm")
    println("     # -> Abort")
    println("A -> Stations Cnt")
    println("B -> Coins Cnt")
    println("C -> Reset Counters")
    println("D -> Shutdown")
    TicketMachineApp.program()
}
