import isel.leic.utils.Time
import java.util.Date
import java.text.SimpleDateFormat

//======================================================================================================================
//                                                TEXT USER INTERFACE
//======================================================================================================================
object TUI {
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
    /**
     * Protected Function: center()
     *
     * Description: Calculates the starting column required to center a text on the LCD
     * @param text Text to be centered
     * @return Starting column position
     */
    private fun center(text: String): Int {
        val getCol = (LCD.COLS - text.length) / 2
        return if (getCol < 0) 0 else getCol
    }
    /**
     * Function: write()
     *
     * Description: Improved writing function. Writes text to the LCD with optional centering and clearing
     * @param text Text to be displayed
     * @param lin LCD line position
     * @param col LCD column position
     * @param center Enables text centering
     * @param clear Clears the LCD before writing
     * @return void
     * @see LCD.clear
     * @see LCD.cursor
     * @see LCD.write
     */
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
    /**
     * Function: init()
     *
     * Description: Initializes the LCD and Keyboard modules
     * @param void
     * @return void
     * @see LCD
     * @see KBD
     */
    fun init() {
        LCD.init()
        KBD.init()
        LCD.hideCursor()
    }
    /**
     * Function: startMenu()
     *
     * Description: Displays the system home screen with current date and time
     * @param void
     * @return void
     * @see SimpleDateFormat
     */
    fun startMenu() {
        write("${5.toChar()}ByteTrip!${5.toChar()}", 0, 0, true, true)
        val dateStr = dateFormatter.format(Date())
        write(dateStr, 1, 0, true, false)
    }
    /**
     * Function: maintenanceScreen()
     *
     * Description: Displays the maintenance screen with the selected option
     * @param option Maintenance option to display
     * @return void
     */
    fun maintenanceScreen(option: String) {
        write("${6.toChar()}Maintenance${7.toChar()}", 0, 0, true, true)
        write(option, 1, 0, true, false)
    }
    /**
     * Function: writeKeyOnLCD()
     *
     * Description: Writes a pressed key to the LCD
     * @param key Character to display
     * @return void
     * @see LCD.write
     */
    fun writeKeyOnLCD(key: Char) {
        LCD.write(key.toString())
    }
    /**
     * Function: yesOrNo()
     *
     * Description: Displays a confirmation screen and waits for user input
     * @param text Question to display
     * @param time Timeout in milliseconds
     * @return True if '*' is pressed, false otherwise
     * @see KBD.waitKey
     */
    fun yesOrNo(text: String, time: Long): Boolean {
        write(text, 0, 0, true, true)
        write("*>Yes   Other>No", 1, 0, true, false)
        return KBD.waitKey(time) == '*'
    }
    /**
     * Function: vendingAborted()
     *
     * Description: Displays a vending aborted message
     * @param void
     * @return void
     */
    fun vendingAborted() {
        write("Vending Aborted.", 0, 0, true, true)
    }
    /**
     * Function: vendingAborted2()
     *
     * Description: Displays a vending aborted message and returned amount
     * @param price Amount returned to the user
     * @return void
     */
    fun vendingAborted2(price: Double) {
        write("Vending Aborted.", 0, 0, true, true)
        write("Returned ${"%.2f".format(price)}${0.toChar()}", 1, 0, true, false)
    }
    /**
     * Function: collectTicket()
     *
     * Description: Displays ticket collection instruction
     * @param station Destination station name
     * @return void
     */
    fun collectTicket(station: String) {
        write(station, 0, 0, true, true)
        write("${6.toChar()}Collect Ticket${7.toChar()}", 1, 0, true, false)
    }
    /**
     * Function: collectFinished()
     *
     * Description: Displays a thank-you message after ticket collection
     * @param void
     * @return void
     */
    fun collectFinished() {
        write("Thank You!", 0, 0, true, true)
        write("Have a nice trip", 1, 0, true, false)
    }
    /**
     * Function: toPrint()
     *
     * Description: Displays ticket printing confirmation screen
     * @param station Destination station name
     * @param rt Indicates if the ticket is round trip
     * @return void
     */
    fun toPrint(station: String, rt: Boolean) {
        write(station, 0, 0, true, true)
        if (rt) write("${1.toChar()} * > To Print", 1, 0, true, false)
        if (!rt) write("${1.toChar()}${2.toChar()} * > To Print", 1, 0, true, false)
    }
    /**
     * Function: printTicket()
     *
     * Description: Displays ticket information before printing
     * @param station Destination station name
     * @param keyCode Station selection code
     * @param price Ticket price
     * @return void
     */
    fun printTicket(station: String, keyCode: Int, price: Double) {
        write(station, 0, 0, true, true)
        write("${"%02d".format(keyCode)}${1.toChar()}${2.toChar()}       ${"%.2f".format(price)}${0.toChar()}"
            , 1, 0, true, false)
    }
    /**
     * Function: printTicket2()
     *
     * Description: Displays ticket information including round-trip calculation
     * @param station Destination station name
     * @param rt Indicates if the ticket is round trip
     * @param price Base ticket price
     * @return void
     */
    fun printTicket2(station: String, keyCode: Int, rt: Boolean, price: Double) {
        val price2 = price * if (rt) 2 else 1
        write(station, 0, 0, true, true)
        if (rt) write("${"%02d".format(keyCode)}${1.toChar()}${2.toChar()}       ${"%.2f".format(price)}${0.toChar()}", 1, 0, false, false)
        if (!rt) write("${"%02d".format(keyCode)}${1.toChar()}        ${"%.2f".format(price)}${0.toChar()}", 1, 0, false, false)
    }
    /**
     * Function: processing()
     *
     * Description: Displays a processing message
     * @param station Destination station name
     * @return void
     */
    fun processing(station: String) {
        write(station, 0, 0, true, true)
        write("Processing", 1, 0, false, false)
        for (i in 1..3) {
            val blocks = "${3.toChar()}".repeat(i) + "${4.toChar()}".repeat(3 - i)
            write(blocks, 1, 12, false, false)
            Time.sleep(1000)
        }
    }
    /**
     * Function: printCoins()
     *
     * Description: Displays coin information in maintenance mode
     * @param value Coin value
     * @param amount Number of available coins
     * @param code Coin identifier code
     * @return void
     */
    fun printCoins(value: Double, amount: Int, code: Int) {
        write("${"%.2f".format(value)}${0.toChar()}", 0, 0, true, true)
        write("${"%02d".format(code)}${1.toChar()}${2.toChar()}          $amount",
            1, 0, false, false)
    }
    /**
     * Function: stationCount()
     *
     * Description: Displays station ticket sales information
     * @param station Station name
     * @param idx Station identifier code
     * @param count Number of tickets sold
     * @return void
     */
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
    println("${Miscellaneous.CYAN}${Miscellaneous.BOLD}■ Select to skip to desired test")
    println("${Miscellaneous.GREEN}${Miscellaneous.BOLD}■ Keyboard Test -> 0\n■ Y/N Test -> 1\n■ Interfaces -> 2")
    print("${Miscellaneous.CYAN}${Miscellaneous.BOLD}■ Initializing⬝")
    Time.sleep(1000)
    print("⬝")
    Time.sleep(1000)
    print("⬝\n")
    Time.sleep(1000)
    println("=============================================================================${Miscellaneous.RESET}")
    while (true) {
        TUI.startMenu()
        val testSelect = KBD.waitKey(100000)
        when (testSelect) {
            '0' -> {
                println("${Miscellaneous.YELLOW}${Miscellaneous.BOLD}■ Press C to clear display")
                println("${Miscellaneous.YELLOW}${Miscellaneous.BOLD}■ Press D to quit")
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
                println("${Miscellaneous.YELLOW}${Miscellaneous.BOLD}■ Press D to quit")
                println("=============================================================================")
                TUI.write("Test2: Yes or No", 0, 0, true, true)
                Time.sleep(1000)
                TUI.write("Initializing...", 1, 0, true, false)
                Time.sleep(3000)
                LCD.clear()
                val prompts = arrayOf("LEIC melhor curso?", "Cereais primeiro?", "Messi > CR7")
                for (i in prompts) {
                    if (KBD.getKey() == 'D') break
                    val yN = TUI.yesOrNo(i, 5000)
                    TUI.write(if (yN) "You choose: Yes" else "You choose: No", 0, 0, true, true)
                    Time.sleep(2000)
                }
            }
            '2' -> {
                println("${Miscellaneous.YELLOW}${Miscellaneous.BOLD}■ Press D to quit")
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
                        '7' -> TUI.printCoins(2.55, 11, 8)
                        '8' -> TUI.stationCount("Wakanda", 7, 2)
                        '9' -> TUI.printTicket2("Caraibas", 16, false, 2.25)
                        'D' -> break
                    }
                }
            }
        }
    }
}