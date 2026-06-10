import isel.leic.utils.Time

//======================================================================================================================
//                                              LIQUID CRYSTAL DISPLAY
//======================================================================================================================
object LCD {
    /**Display Dimension**/
    const val LINES = 2
    const val COLS = 16
    /**
     * Protected Function: writeByteSerial()
     *
     * Description: Serially writes a command/data byte on LCD,
     * through a serial emitter
     * @param rs Register select - Selection between a command and a data
     * @param data data to be sent
     * @return void
     * @see SerialEmitter.send
     */
    private fun writeByteSerial(rs: Boolean, data: Int) {
        val rsBit = if (rs) 1 else 0
        val frame1 = rsBit or (data shl 1) or (1 shl 9)
        val frame2 = rsBit or (data shl 1)
        SerialEmitter.send(SerialEmitter.Peripherial.LCD, frame1)
        SerialEmitter.send(SerialEmitter.Peripherial.LCD, frame2)
    }
    /**
     * Protected Function: writeByte()
     *
     * Description: Writes a command/data byte on LCD.
     * @param rs Register Select - Selection between a command and a data
     * @param data data to be sent
     * @return void
     * @see writeByteSerial
     */
    private fun writeByte(rs: Boolean, data: Int) {
        writeByteSerial(rs, data)
    }
    /**
     * Protected Function: writeCMD()
     *
     * Description: Writes a command on LCD.
     * @param data Command to be sent (RS on high)
     * @return void
     * @see writeByte
     */
    private fun writeCMD(data: Int) {
        writeByte(false, data)
    }
    /**
     * Protected Function: writeDATA()
     *
     * Description: Writes data on LCD.
     * @param data Data to be sent (RS on low)
     * @return void
     * @see writeByte
     */
    private fun writeDATA(data: Int) {
        writeByte(true, data)
    }
    /**
     * Protected Function: createChar()
     *
     * Description: Creates & registers special characters on LCD memory
     * @param location Memory address for char to be stored
     * @param pattern Array of the custom char which represents the matrix of pixels
     * @return void
     * @see writeDATA
     * @see writeCMD
     */
    private fun createChar(location: Int, pattern: IntArray) {
        val address = (location and 0x07) shl 3
        writeCMD(0x40 or address)
        for (i in 0..7) {
            writeDATA(pattern[i])
            Time.sleep(2)
        }
        writeCMD(0x80)
    }
    /**
     * Function: init()
     *
     * Description: This functions inits the LCD for communication, display mode,
     * and loads special chars. Sends the command sequence needed for 8-bit communication.
     * @param void
     * @return void
     * @see writeCMD
     * @see Time.sleep
     * @see createChar
     */
    fun init() {
        writeCMD(0b0011_0000)
        Time.sleep(20)
        writeCMD(0b0011_0000)
        Time.sleep(20)
        writeCMD(0b0011_0000)
        writeCMD(0b0011_1000)
        writeCMD(0b0000_1000)
        writeCMD(0b0000_0001)
        writeCMD(0b0000_0110)
        writeCMD(0b0000_1111)
        createChar(0, euro)
        createChar(1, upArrow)
        createChar(2, downArrow)
        createChar(3, fullBlock)
        createChar(4, emptyBlock)
        createChar(5, upTri)
        createChar(6, rightTri)
        createChar(7, leftTri)
    }
    /**
     * Function: write()
     *
     * Description: writes a Char in current cursor position
     * @param c Char to be displayed
     * @return void
     * @see writeDATA
     */
    fun write(c: Char) {
        writeDATA(c.code)
    }
    /**
     * Function: write()
     *
     * Description: writes a String/Char sequence from current cursor position
     * @param text String to be displayed
     * @return void
     * @see writeDATA
     */
    fun write(text: String) {
        for (i in text)
            writeDATA(i.code)
    }
    /**
     * Function: cursor()
     *
     * Description: Positions the cursor on specified line & column
     * @param line line to position
     * @param column column to position
     * @return void
     * @see writeCMD
     */
    fun cursor(line: Int, column: Int) {
        writeCMD((line * 0x40 + column) or 0x80)
    }
    /**
     * Function: hideCursor()
     *
     * Description: Hides the cursor (misc)
     * @param void
     * @return void
     * @see writeCMD
     */
    fun hideCursor() {
        LCD.writeCMD(0x0C)
    }
    /**
     * Function: showCursor()
     *
     * Description: Shows the cursor (misc)
     * @param void
     * @return void
     * @see writeCMD
     */
    fun showCursor() {
        LCD.writeCMD(0x0F)
    }
    /**
     * Function: clear()
     *
     * Description: Sends command to clear the display and set the cursor
     * on coordinates (0,0)
     * @param void
     * @return void
     * @see writeCMD
     * @see cursor
     */
    fun clear() {
        writeCMD(0x01)
        cursor(0, 0)
    }
}
//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    LCD.init()
    println("${Miscellaneous.CYAN}${Miscellaneous.BOLD}■ Write the text you want to see displayed")
    print("${Miscellaneous.CYAN}${Miscellaneous.BOLD}■ Initializing⬝")
    Time.sleep(1000)
    print("⬝")
    Time.sleep(1000)
    print("⬝\n")
    Time.sleep(1000)
    println("=============================================================================${Miscellaneous.RESET}")
    while(true) {
        print("${Miscellaneous.YELLOW}Write: ")
        val text = readln()
        LCD.clear()
        LCD.write(text)
    }
}

