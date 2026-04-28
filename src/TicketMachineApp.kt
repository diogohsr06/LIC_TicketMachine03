/**On going**/
object TicketMachineApp {
    fun init() {
        TUI.init()
        TicketDispenser.init()
    }
}

fun main() {
    TicketMachineApp.init()
    TUI.writeKeyOnLCD()
}