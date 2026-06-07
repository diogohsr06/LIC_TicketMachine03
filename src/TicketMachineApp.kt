import isel.leic.utils.Time
import kotlin.system.exitProcess

//======================================================================================================================
//                                                 TICKET MACHINE
//======================================================================================================================
object TicketMachineApp {
    /**
     * Origin station (protected)
     *
     * Key timeout (protected)
     */
    private const val ORIGIN_STATION = 0
    private const val WAIT_KEY_MS = 2000L

    /**
     * Function: maintenanceMode()
     *
     * Description: Maintenance mode of the application
     * @param void
     * @return void
     */
    private fun maintenanceMode() {
        val options = listOf(
            "#-Print Ticket",
            "A-Station Cnt.",
            "B-Coins Cnt.",
            "C-Reset Cnt.",
            "D-Shutdown"
        )
        var optIdx = 0
        /**
         * Function: printTicket_M()
         *
         * Description: Simulates ticket printing
         * @param void
         * @return void
         * @see KBD.waitKey
         * @see TUI.printTicket
         * @see M.enabled
         * @see TUI.toPrint
         * @see TUI.processing
         * @see TicketDispenser.lowerPrt
         * @see TicketDispenser.activatePrintingTicket
         * @see TUI.collectFinished
         * @see TUI.collectTicket
         * @see TUI.vendingAborted
         */
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
                            Time.sleep(1200)
                            continue
                        }
                        TUI.processing(st.station)
                        Time.sleep(3000)
                        TicketDispenser.activatePrintingTicket(roundTrip, ORIGIN_STATION, idx)
                        TUI.collectTicket(st.station)
                        while (!HAL.isBit(INPUTPORTS.FN.mask)) {
                            Time.sleep(10)
                        }
                        TicketDispenser.lowerPrt(roundTrip, ORIGIN_STATION, idx)
                        Time.sleep(200)
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
        /**
         * Function: stationCnt()
         *
         * Description: Displays counters for stations
         * @param void
         * @return void
         * @see KBD.waitKey
         * @see M.enabled
         * @see TUI.stationCount
         */
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
        /**
         * Function: coinsCnt()
         *
         * Description: Display counters for coins
         * @param void
         * @return void
         * @see KBD.waitKey
         * @see TUI.printCoins
         * @see M.enabled
         * @see CoinDeposit.getCount
         */
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
        /**
         * Function: resetCounters()
         *
         * Description: Resets counters
         * @param void
         * @return void
         * @see TUI.yesOrNo
         * @see TUI.write
         * @see Stations.reset
         * @see CoinDeposit.resetCnt
         */
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
        /**
         * Function: shutdown()
         *
         * Description: Saves everything and closes process
         * @param void
         * @return void
         * @see Stations.save
         * @see CoinDeposit.saveCoins
         * @see TUI.write
         */
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
                '#' -> printTicket_M()
                'A' -> stationCnt()
                'B' -> coinsCnt()
                'C' -> resetCounters()
                'D' -> if (TUI.yesOrNo("Shutdown?", 5000L)) shutDown()
                KBD.none -> optIdx = (optIdx + 1) % options.size
            }
        }
    }
    /**
     * Function: normalMode()
     *
     * Description: Vending mode of the application
     * @param void
     * @return void
     */
    private fun normalMode() {
        val stations = Stations.stations
        if (stations.isEmpty()) return
        /**
         * Function: pollCoin()
         *
         * Description: Manages the hardware (Coin acceptor) and processes acceptance of new coins.
         * Updates the deposit
         * @param void
         * @return void
         * @see CoinAcceptor.coinInserted
         * @see CoinAcceptor.coinAccept
         * @see CoinDeposit.insert
         */
        fun pollCoin() {
            if (!CoinAcceptor.coinInserted()) return
            val id = CoinAcceptor.getCoinId()
            if (id in 0..5) {
                CoinDeposit.insert(id)
            }
            CoinAcceptor.coinAccept()
        }
        /**
         * Function: abortVending()
         *
         * Description: Cancels the current transaction. Returns inserted coins.
         * Updates the deposit
         * @param void
         * @return void
         * @see CoinAcceptor.coinReturn
         * @see CoinDeposit.cancel
         * @see CoinDeposit.getTotal
         * @see TUI.vendingAborted
         * @see TUI.vendingAborted2
         */
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
        /**
         * Function: waitForPayment()
         *
         * Description: Waits for the insertion of the value by the user
         * @param station Destination
         * @param required Price
         * @return Boolean
         * @see Others.centsToEuros
         * @see TUI.write
         * @see pollCoin
         * @see M.enabled
         * @see CoinDeposit.getTotal
         * @see KBD.waitKey
         */
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
    /**
     * Function: init()
     *
     * Description: This functions inits the object
     * @param void
     * @return void
     * @see M.init
     * @see CoinDeposit.init
     * @see CoinAcceptor.init
     * @see TicketDispenser.init
     * @see TUI.init
     * @see Stations.init
     */
    fun init() {
        M.init()
        CoinAcceptor.init()
        TicketDispenser.init()
        TUI.init()
        CoinDeposit.init()
        Stations.init()
    }
    /**
     * Function: Program()
     *
     * Description: The core of the APP
     * @param void
     * @return void
     * @see TUI.startMenu
     * @see M.enabled
     * @see maintenanceMode
     * @see normalMode
     * @see KBD.waitKey
     */
    fun program() {
        while (true) {
            TUI.startMenu()
            if (M.enabled()) maintenanceMode()
            if (KBD.waitKey(WAIT_KEY_MS) == '#') normalMode()
        }
    }
}

//======================================================================================================================
//                                                      MAIN
//======================================================================================================================
fun main() {
    TicketMachineApp.init()
    print("■ Initializing⬝")
    Time.sleep(1000)
    print("⬝")
    Time.sleep(1000)
    print("⬝\n")
    Time.sleep(1000)
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
