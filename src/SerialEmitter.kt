import isel.leic.utils.Time

//======================================================================================================================
//                                                  SERIAL EMITTER
//======================================================================================================================
object SerialEmitter {
    enum class Peripherial {LCD, TICKET}
    /**
     * Function: init()
     *
     * Description: This functions inits the object
     * @param void
     * @return void
     * @see HAL.init
     * @see HAL.setBits
     * @see HAL.clrBits
     */
    fun init() {
        HAL.init() //port a 0
        HAL.setBits(OUTPUTPORTS.SS_LCD.mask)
        HAL.setBits(OUTPUTPORTS.SS_TD.mask)
        HAL.clrBits(OUTPUTPORTS.SCLK.mask)
    }
    /**
     * Function: send()
     *
     * Description: This functions sends frames of 10 bits of data to a
     * specified peripheral
     * @param addr Peripheral of interest
     * @param data Data to be sent
     * @return void
     * @see OUTPUTPORTS
     * @see HAL.setBits
     * @see HAL.clrBits
     */
    fun send(addr: Peripherial, data: Int) {
        val SS = if (addr == Peripherial.LCD) OUTPUTPORTS.SS_LCD.mask else OUTPUTPORTS.SS_TD.mask
        HAL.clrBits(SS)
        for (i in 0 ..9) {
            HAL.clrBits(OUTPUTPORTS.SCLK.mask)
            val SDX = data shr i and 1
            if (SDX == 1) {
                HAL.setBits(OUTPUTPORTS.SDX.mask)
            } else HAL.clrBits(OUTPUTPORTS.SDX.mask)
            HAL.setBits(OUTPUTPORTS.SCLK.mask)
        }
        HAL.setBits(SS)
    }
}
//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    SerialEmitter.init()
    println("${Miscellaneous.CYAN}${Miscellaneous.BOLD}■ Write the data you want to be sent on Peripherals")
    println("${Miscellaneous.YELLOW}${Miscellaneous.BOLD}■ Data is recommended to be written on 10 bits for better understanding")
    print("${Miscellaneous.CYAN}${Miscellaneous.BOLD}■ Initializing⬝")
    Time.sleep(1000)
    print("⬝")
    Time.sleep(1000)
    print("⬝\n")
    Time.sleep(1000)
    println("=============================================================================${Miscellaneous.RESET}")
    while (true) {
        print("${Miscellaneous.GREEN}Data to LCD: ")
        val dataLCD = readln()
        val dataLCDbin = if (dataLCD.length == 10 && dataLCD.all { it == '0' || it == '1' }) dataLCD.toInt(2) else dataLCD.toInt()
        print("${Miscellaneous.GREEN}Data to TD: ")
        val dataTD = readln()
        val dataTDbin = if (dataTD.length == 10 && dataTD.all { it == '0' || it == '1' }) dataTD.toInt(2) else dataTD.toInt()
        SerialEmitter.send(SerialEmitter.Peripherial.LCD, dataLCDbin)
        SerialEmitter.send(SerialEmitter.Peripherial.TICKET, dataTDbin)
        print("${Miscellaneous.GREEN}■ Sent!\n")
        println("${Miscellaneous.GREEN}${Miscellaneous.BOLD}--------------------------------------------------------------------")
    }
}