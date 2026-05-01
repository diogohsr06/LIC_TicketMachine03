import isel.leic.utils.Time
import java.util.Date
import java.text.SimpleDateFormat

/**Text User Interface**/
object TUI {
    val date = SimpleDateFormat("dd/MM/yyyy hh:mm").format(Date())
    private fun center(text: String): Int {
        val getCol = (LCD.COLS - text.length) / 2
        return if (getCol < 0) 0 else getCol
    }
    fun write(text: String, lin: Int, col: Int, center: Boolean, clear: Boolean) {
        if (clear) LCD.clear()
        if (text.length <= LCD.COLS) {
            val startCol = if (center) center(text) else col
            LCD.cursor(lin, startCol)
            LCD.write(text)
        } else {
            val line0Text = text.substring(0, LCD.COLS)
            val startCol0 = if (center) center(line0Text) else 0
            LCD.cursor(0, startCol0)
            LCD.write(line0Text)
            val remaining = text.substring(LCD.COLS)
            val line1Text = if (remaining.length > LCD.COLS) remaining.substring(0, LCD.COLS) else remaining
            val startCol1 = if (center) center(line1Text) else 0
            LCD.cursor(1, startCol1)
            LCD.write(line1Text)
        }
    }
    fun init() {
        LCD.init()
        KBD.init()
    }
    fun startMenu() {
        LCD.clear()
        write("Ticket to Ride", 0, 0, true, true)
        val dateStr = date.format(Date())
        write(dateStr, 1, 0, true, false)
    }
    fun writeKeyOnLCD() {
        LCD.write(KBD.waitKey(10000))
    }
    fun yesOrNo(text: String, time: Long): Boolean {
        write(text, 0, 0, true, true)
        write("*-Yes  other-No", 1, 0, true, false)
        return KBD.waitKey(time) == '*'
    }
    fun printTicket(station: String, rt: Boolean, price: Double) {
        TODO()
    }
}

/**Teste**/
fun main() {
    TUI.init()
    TUI.startMenu()
    Time.sleep(5000)

    TUI.write("Test1: Press Key", 0, 0, true, true)
    TUI.write("Only one: ", 1, 0, false, false)
    Time.sleep(1000)
    TUI.writeKeyOnLCD()
    Time.sleep(5000)

    TUI.write("Test2: Yes/No", 0, 0, true, true)
    Time.sleep(1000)
    val text = "die."
    LCD.clear()
    val yN = TUI.yesOrNo(text,5000)
    if (yN) {
        TUI.write("You chose Yes", 0, 0, true, true)
    } else {
        TUI.write("nigga.", 0, 0, true, true)
    }
    Time.sleep(5000)

    TUI.write("Test3: Printing Tickets", 0, 0, true, true)
    Time.sleep(1000)
    TUI.printTicket("Lisboa", true, 2.25)
    Time.sleep(3000)
    TUI.printTicket("Maldivas", false, 4.55)
}
