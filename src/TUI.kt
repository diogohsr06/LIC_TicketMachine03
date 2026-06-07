import isel.leic.utils.Time
import java.util.Date
import java.text.SimpleDateFormat

//======================================================================================================================
//                                                TEXT USER INTERFACE
//======================================================================================================================
object TUI {
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
    /**Calculates the centered column**/
    private fun center(text: String): Int {
        val getCol = (LCD.COLS - text.length) / 2
        return if (getCol < 0) 0 else getCol
    }
    /**Improved LCD writing function, able to center, clear, set cursor and fix display bounds in one call**/
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
    /**Inits the object**/
    fun init() {
        LCD.init()
        KBD.init()
    }
    /**Home screen**/
    fun startMenu() {
        write("Ticket to Ride", 0, 0, true, true)
        val dateStr = dateFormatter.format(Date())
        write(dateStr, 1, 0, true, false)
    }
    /**Maintenance screen**/
    fun maintenanceScreen(option: String) {
        write("Maintenance", 0, 0, true, true)
        write(option, 1, 0, true, false)
    }
    /**Writes the key pressed**/
    fun writeKeyOnLCD(key: Char) {
        LCD.write(key.toString())
    }
    /**Yes or No screen**/
    fun yesOrNo(text: String, time: Long): Boolean {
        write(text, 0, 0, true, true)
        write("*-Yes   other-No", 1, 0, true, false)
        return KBD.waitKey(time) == '*'
    }
    /**Vending aborted**/
    fun vendingAborted() {
        write("Vending Aborted.", 0, 0, true, true)
    }
    /**Vending aborted with returned value**/
    fun vendingAborted2(price: Double) {
        write("Vending Aborted.", 0, 0, true, true)
        write("Returned ${"%.2f".format(price)}${0.toChar()}", 1, 0, true, false)
    }
    /**Collect ticket screen**/
    fun collectTicket(station: String) {
        write(station, 0, 0, true, true)
        write("Collect Ticket", 1, 0, true, false)
    }
    /**Post ticket collection screen**/
    fun collectFinished() {
        write("Thank You!", 0, 0, true, true)
        write("Have a nice trip", 1, 0, true, false)
    }
    /**Station to print screen**/
    fun toPrint(station: String, rt: Boolean) {
        write(station, 0, 0, true, true)
        if (rt) write("${1.toChar()} *- to Print", 1, 0, true, false)
        if (!rt) write("${1.toChar()}${2.toChar()} *- to Print", 1, 0, true, false)
    }
    /**Station selection screen**/
    fun printTicket(station: String, keyCode: Int, price: Double) {
        write(station, 0, 0, true, true)
        write("${"%02d".format(keyCode)}${1.toChar()}${2.toChar()}       ${"%.2f".format(price)}${0.toChar()}"
            , 1, 0, true, false)
    }
    /**Station selection screen with roundtrip**/
    fun printTicket2(station: String, rt: Boolean, price: Double) {
        val price2 = price * if (rt) 2 else 1
        write(station, 0, 0, true, true)
        if (rt) write("${1.toChar()}${2.toChar()}         ${"%.2f".format(price2)}${0.toChar()}", 1, 0, false, false)
        if (!rt) write("${1.toChar()}          ${"%.2f".format(price2)}${0.toChar()}", 1, 0, false, false)
    }
    /**Processing screen**/
    fun processing(station: String) {
        write(station, 0, 0, true, true)
        write("Processing ...", 1, 0, true, false)
    }
    /**Coins Count (Maintenance)**/
    fun printCoins(value: Double, amount: Int, code: Int) {
        write("${"%.2f".format(value)}${0.toChar()}", 0, 0, true, true)
        write("${"%02d".format(code)}${1.toChar()}${2.toChar()}           $amount",
            1, 0, false, false)
    }
    /**Station Count screen (Maintenance)**/
    fun stationCount(station: String, idx: Int, count: Int) {
        write(station, 0, 0, true, true)
        write("${"%02d".format(idx)}${1.toChar()}${2.toChar()}           $count"
            , 1, 0, false, false)
    }
}

//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    TUI.init()
    println("■ Select to skip to desired test")
    println("■ Keyboard Test -> 0\n■ Y/N Test -> 1\n■ Interfaces -> 2")
    println("■ Initializing...")
    Time.sleep(3000)
    println("=============================================================================")
    while (true) {
        TUI.startMenu()
        val testSelect = KBD.waitKey(100000)
        when (testSelect) {
            '0' -> {
                println("■ Press C to clear display")
                println("■ Press D to quit")
                println("=============================================================================")
                TUI.write("Test1: Keyboard", 0, 0, true, true)
                Time.sleep(1000)
                TUI.write("Initializing...", 1, 0, true, false)
                Time.sleep(3000)
                LCD.clear()
                TUI.write("> ", 0, 0, false, true)
                while (true) {
                    val key = KBD.getKey()
                    if (key != KBD.none) {
                        when (key) {
                            'D' -> break
                            'C' -> {
                                LCD.clear()
                                TUI.write("> ", 0, 0, false, true)
                            }
                            else -> TUI.writeKeyOnLCD(key)
                        }
                    }
                }
            }
            '1' -> {
                println("■ Press D to quit")
                println("=============================================================================")
                TUI.write("Test2: Yes or No", 0, 0, true, true)
                Time.sleep(1000)
                TUI.write("Initializing...", 1, 0, true, false)
                Time.sleep(3000)
                LCD.clear()
                val prompts = arrayOf("LEIC melhor curso?", "Cereais primeiro?", "Messi > CR7")
                for (pergunta in prompts) {
                    if (KBD.getKey() == 'D') break
                    val yN = TUI.yesOrNo(pergunta, 5000)
                    TUI.write(if (yN) "You choose: Yes" else "You choose: No", 0, 0, true, true)
                    Time.sleep(2000)
                }
            }
            '2' -> {
                println("■ Press D to quit")
                println("=============================================================================")
                TUI.write("Interfaces", 0, 0, true, true)
                Time.sleep(1000)
                TUI.write("Initializing...", 1, 0, true, false)
                Time.sleep(3000)
                LCD.clear()
                TUI.write("Use the keyboard to navigate", 0, 0, true, true)
                while (true) {
                    val key = KBD.getKey()
                    when (key) {
                        '0' -> TUI.printTicket("Maldivas", 13, 4.55)
                        '1' -> TUI.vendingAborted()
                        '2' -> TUI.vendingAborted2(0.67)
                        '3' -> TUI.collectTicket("Roma")
                        '4' -> TUI.collectFinished()
                        '5' -> TUI.toPrint("Atenas", true)
                        '6' -> TUI.processing("Troia")
                        '7' -> TUI.printCoins(2.55, 4, 8)
                        '8' -> TUI.stationCount("Wakanda", 7, 2)
                        '9' -> TUI.printTicket2("Caraibas", true, 2.25)
                        'D' -> break
                    }
                }
            }
        }
    }
}