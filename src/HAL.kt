import isel.leic.UsbPort
import isel.leic.utils.Time

//======================================================================================================================
//                                              HARDWARE ABSTRACT LAYER
//======================================================================================================================
object HAL {
    /**Output Port state (protected)**/
    private var usbport = 0
    /**
     * Function: init()
     *
     * Description: This functions inits the object
     * @param void
     * @return void
     * @see UsbPort
     */
    fun init() {
        usbport = 0
        UsbPort.write(usbport)
    }
    /**
     * Function: readBits()
     *
     * Description: This functions reads the value of the bits,
     * filtered by mask.
     * @param mask Filter out the bit/s of interest.
     * @return UsbPort.read() and mask. This is the value written on Input ports.
     * @see UsbPort
     */
    fun readBits(mask: Int): Int {
        return UsbPort.read() and mask
    }
    /**
     * Function: isBit()
     *
     * Description: Checks the logic value of a bit
     * @param mask Filter out the bit/s of interest.
     * @return (UsbPort.read() and mask != 0). This is the logic value of the bit filtered.
     * @see UsbPort
     */
    fun isBit(mask: Int): Boolean {
        return (UsbPort.read() and mask != 0)
    }
    /**
     * Function: setBits()
     *
     * Description: Sets the filtered bits with logic value '1'
     * @param mask Filter out the bit/s of interest.
     * @return void
     * @see UsbPort
     */
    fun setBits(mask: Int) {
        usbport = usbport or mask
        UsbPort.write(usbport)
    }
    /**
     * Function: clrBits()
     *
     * Description: Sets the filtered bits with logic value '0'
     * @param mask Filter out the bit/s of interest.
     * @return void
     * @see UsbPort
     */
    fun clrBits(mask: Int) {
        usbport = usbport and mask.inv()
        UsbPort.write(usbport)
    }
    /**
     * Function: writeBits()
     *
     * Description: Sets the filtered bits with 'value'
     * @param mask Filter out the bit/s of interest.
     * @param value Value to be written on bits of interest
     * @return void
     * @see UsbPort
     */
    fun writeBits(mask: Int, value: Int) {
        usbport = (usbport and mask.inv()) or (value and mask)
        UsbPort.write(usbport)
    }
}

//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    HAL.init()
    print("■ Initializing⬝")
    Time.sleep(1000)
    print("⬝")
    Time.sleep(1000)
    print("⬝\n")
    Time.sleep(1000)
    println("=============================================================================")
    println("■ Choose a task:")
    println("0..7 - Toggle bit")
    println("8 - Read Port")
    println("9 - Set masked bits")
    println("10 - Clear masked bits")
    println("11 - Quit")
    println("======================================")
    var quit = false
    while (!quit) {
        val portState = UsbPort.read()
        print("\r■ Current Port State: 0b${portState.toString(2).padStart(8, '0')} | Dec: $portState\n")
        print("\r> ")
        val key = readln().toInt()
            when {
                key in 0..7 -> {
                    val bitIndex = key
                    val mask = 1 shl bitIndex
                    if (HAL.isBit(mask)) HAL.clrBits(mask) else HAL.setBits(mask)
                    println("■ Toggled bit $bitIndex")
                }
                key == 8 -> {
                    print("Mask to read (8 bits): ")
                    val mask = readln().toIntOrNull(2) ?: 0
                    println("■ Port read: ${HAL.readBits(mask)}")
                }
                key == 9 -> {
                    print("Mask to set (8 bits): ")
                    val mask = readln().toIntOrNull(2) ?: 0
                    HAL.setBits(mask)
                }
                key == 10 -> {
                    print("Mask to clear (8 bits): ")
                    val mask = readln().toIntOrNull(2) ?: 0
                    HAL.clrBits(mask)
                }
                key == 11 -> quit = true
            }
        Time.sleep(100)
    }
}