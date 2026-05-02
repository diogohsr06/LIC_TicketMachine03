import isel.leic.utils.Time
import java.util.Date
import java.text.SimpleDateFormat

/**Text User Interface**/
object TUI {
    val date = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date())
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
        write("*-Yes   other-No", 1, 0, true, false)
        return KBD.waitKey(time) == '*'
    }
    fun vendingAborted() {
        write("Vending Aborted.", 0, 0, true, true)
    }
    fun vendingAborted2(price: Double) {
        write("Vending Aborted.", 0, 0, true, true)
        write("Returned ${"%.2f".format(price)}${0.toChar()}", 1, 0, true, false)
    }
    fun collectTicket(station: String) {
        write(station, 0, 0, true, true)
        write("Collect Ticket", 1, 0, true, false)
    }
    fun collectFinished() {
        write("Thank You!", 0, 0, true, true)
        write("Have a nice trip", 1, 0, true, false)
    }
    fun toPrint(station: String, rt: Boolean) {
        write(station, 0, 0, true, true)
        if (rt) write("${1.toChar()} *- to Print", 1, 0, true, false)
        if (!rt) write("${1.toChar()}${2.toChar()} *- to Print", 1, 0, true, false)
    }
    fun printTicket(station: String, keyCode: Int, price: Double) {
        write(station, 0, 0, true, true)
        write("${"%02d".format(keyCode)}${1.toChar()}${2.toChar()}       ${"%.2f".format(price)}${0.toChar()}"
            , 1, 0, true, false)
    }
    fun processing(station: String) {
        write(station, 0, 0, true, true)
        write("Processing ...", 1, 0, true, false)
    }
    fun printCoins(value: Double, amount: Int, code: Int) {
        write("${"%.2f".format(value)}${0.toChar()}", 0, 0, true, true)
        write("${"%02d".format(code)}${1.toChar()}${2.toChar()}           $amount",
            1, 0, false, false)
    }
    fun stationCount(station: String, count: Int) {
        write(station, 0, 0, true, true)
        write("${"%02d".format(count)}${1.toChar()}${2.toChar()}           $count"
            , 1, 0, false, false)
    }
}

/**Teste**/
fun main() {
    TUI.init()
    TUI.startMenu()
    Time.sleep(5000)

    TUI.write("Test1: Press Key", 0, 0, true, true)
    TUI.write("Only one: ", 1, 0, false, false)
    Time.sleep(2000)
    TUI.writeKeyOnLCD()
    Time.sleep(5000)

    TUI.write("Test2: Yes/No", 0, 0, true, true)
    Time.sleep(2000)
    val text = "Fumar faz bem?"
    LCD.clear()
    val yN = TUI.yesOrNo(text,5000)
    if (yN) {
        TUI.write("You chose Yes", 0, 0, true, true)
    } else {
        TUI.write("Faz sim.", 0, 0, true, true)
    }
    Time.sleep(5000)

    TUI.write("Test3: Printing Tickets Interfaces", 0, 0, true, true)
    Time.sleep(2000)
    TUI.printTicket("Lisboa", 1, 2.25)
    Time.sleep(2000)
    TUI.printTicket("Maldivas", 13, 4.55)
    Time.sleep(2000)
    TUI.vendingAborted()
    Time.sleep(2000)
    TUI.vendingAborted2(3.46)
    Time.sleep(2000)
    TUI.collectTicket("Roma")
    Time.sleep(2000)
    TUI.collectFinished()
    Time.sleep(2000)
    TUI.toPrint("Atenas", true)
    Time.sleep(2000)
    TUI.processing("Troia")
    Time.sleep(2000)
    TUI.printCoins(2.55, 4, 8)
    Time.sleep(2000)
    TUI.stationCount("Wakanda", 2)
    Time.sleep(2000)
    TUI.write("Interfaces have been tested!", 0, 0, true, true)
}
