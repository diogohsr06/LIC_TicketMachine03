import TicketMachineApp.program
import isel.leic.utils.Time

/**On going**/
object TicketMachineApp {
    private const val ORIGIN_STATION = 0
    private const val WAIT_KEY_MS = 200L

    private fun maintenanceMode() {
        fun showMInterfaces() {

        }
    }
    private fun normalMode() {
        TODO()
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