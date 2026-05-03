/**On going**/
object TicketMachineApp {
    private fun maintenanceMode() {
        TODO()
    }
    private fun program() {
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
}

fun main() {
    TicketMachineApp.init()
}