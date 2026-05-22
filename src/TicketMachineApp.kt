import TicketMachineApp.program
import isel.leic.utils.Time

/**On going**/
object TicketMachineApp {
    private const val ORIGIN_STATION = 0
    private const val WAIT_KEY_MS = 200L

    private fun maintenanceMode() {
        val options = listOf<String>("#-Print Ticket", "A-Station Cnt.", "B-Coins Cnt.", "C-Reset Cnt.", "D-Shutdown")
        fun showMInterfaces() {
            var i = 0
            while (i < options.size) {
                TUI.maintenanceScreen(options[i])
                Time.sleep(2000)
                i++
                if (i == options.size) i = 0
            }
        }
        showMInterfaces()
    }
    private fun normalMode() {
        TUI.write("Yet to implement", 0, 0, true, true)
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
            Time.sleep(1000)
            if (M.enabled()) maintenanceMode() else normalMode()
        }
    }
}

fun main() {
    TicketMachineApp.init()
    program()
}