import TUI.write
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
                        val nextIdx = (idx % 10) * 10 + digit
                        if (nextIdx < stations.size) {
                            idx = nextIdx
                        } else if (digit < stations.size) {
                            idx = digit
                        }
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
                        val nextIdx = (idx % 10) * 10 + digit
                        if (nextIdx < stations.size) {
                            idx = nextIdx
                        } else if (digit < stations.size) {
                            idx = digit
                        }
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
            LCD.clear()
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
            var lastInserted = -1
            while (!M.enabled()) {
                pollCoin()
                val inserted = CoinDeposit.getTotal()
                if (inserted != lastInserted) {
                    TUI.write(station, 0, 0, true, true)
                    TUI.write(
                        "${6.toChar()}${Utils.centsToEuros(inserted)}/${Utils.centsToEuros(required)}${0.toChar()}${7.toChar()}",
                        1,
                        0,
                        true,
                        false
                    )
                    lastInserted = inserted
                }
                if (inserted >= required) return true
                if (KBD.waitKey(200) == '#') return false
            }
            return false
        }
        var idx = 0
        var roundTrip = false
        var redraw = true
        while (!M.enabled()) {
            val st = stations[idx]
            val price = st.price
            pollCoin()
            TUI.printTicket2(st.station, idx, roundTrip, price / 100.0)
            when (val key = KBD.waitKey(WAIT_KEY_MS)) {
                'A' -> idx = if (idx > 0) idx - 1 else stations.size - 1
                'B' -> idx = if (idx < stations.size - 1) idx + 1 else 0
                'C' -> roundTrip = !roundTrip
                '*' -> {
                    TUI.toPrint(st.station, !roundTrip)
                    if (KBD.waitKey(7000L) != '*') {
                        abortVending()
                        redraw = true
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
                    val nextIdx = (idx % 10) * 10 + digit
                    if (nextIdx < stations.size) {
                        idx = nextIdx
                    } else if (digit < stations.size) {
                        idx = digit
                    }
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
        var scrollIdx = 0
        var lastScrollTime = 0L
        val scrollSpeedMs = 350L
        val welcomeText = "${6.toChar()}Press # to Enter${7.toChar()}        ${6.toChar()}Press M key to Enter Maintenance${7.toChar()}"
        TUI.startMenu()
        Time.sleep(1500)
        while (true) {
            if (M.enabled()) {
                maintenanceMode()
                TUI.startMenu()
                scrollIdx = 0
                lastScrollTime = 0L
            }
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastScrollTime >= scrollSpeedMs) {
                Miscellaneous.writeScrollingText(welcomeText, 1, scrollIdx)
                scrollIdx++
                lastScrollTime = currentTime
            }
            val key = KBD.waitKey(20L)
            if (key == '#') {
                normalMode()
                TUI.startMenu()
                Time.sleep(3000)
                scrollIdx = 0
                lastScrollTime = 0L
            }
        }
    }
}
//======================================================================================================================
//                                                      MAIN
//======================================================================================================================
fun main() {
    TicketMachineApp.init()

    print("${Miscellaneous.CYAN}${Miscellaneous.BOLD}■ Initializing")
    write("Powering", 0, 0, false, false)
    for (i in 1..6) {
        val blocks = "${3.toChar()}".repeat(i) + "${4.toChar()}".repeat(6 - i)
        write(blocks, 0, 9, false, false)
        Time.sleep(500)
    }
    repeat(3) { Time.sleep(600); print("⬝") }
    println("${Miscellaneous.RESET}\n")

    println("${Miscellaneous.GREEN}======================= Navigation in Vending Mode =======================${Miscellaneous.RESET}")
    println("${Miscellaneous.YELLOW}#${Miscellaneous.RESET} -> Enter vending mode")
    println("Inside:")
    println("  ${Miscellaneous.YELLOW}C${Miscellaneous.RESET} -> Select trip type (One-way/Round-trip)")
    println("  ${Miscellaneous.YELLOW}*${Miscellaneous.RESET} -> Confirm Selection")
    println("  ${Miscellaneous.RED}#${Miscellaneous.RESET} -> Abort / Return Coins")

    println("\n${Miscellaneous.GREEN}=================== Navigation in Maintenance Mode ===================${Miscellaneous.RESET}")
    println("  ${Miscellaneous.YELLOW}A${Miscellaneous.RESET} -> Stations Cnt  |  ${Miscellaneous.YELLOW}B${Miscellaneous.RESET} -> Coins Cnt")
    println("  ${Miscellaneous.YELLOW}C${Miscellaneous.RESET} -> Reset Cnt     |  ${Miscellaneous.RED}D${Miscellaneous.RESET} -> Shutdown")

    TicketMachineApp.program()
}
