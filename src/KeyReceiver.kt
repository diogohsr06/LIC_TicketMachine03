import isel.leic.utils.Time

//======================================================================================================================
//                                                      Key Receiver
//======================================================================================================================
object KeyReceiver {
    /**
     * Function: init()
     *
     * Description: This functions inits the object
     * @param void
     * @return void
     * @see HAL.init
     * @see HAL.clrBits
     */
    fun init() {
        HAL.init()
        HAL.clrBits(OUTPUTPORTS.TXclk.mask)
    }
    /**
     * Function: serialReceiver()
     *
     * Description: Builds the frame as data is received serially.
     * Detects protocol errors and isolates the KeyCode.
     * @param void
     * @return Key Code or -1
     * @see HAL.isBit
     * @see HAL.clrBits
     */
    fun serialReceiver(): Int {
        if (HAL.isBit(INPUTPORTS.TXD.mask)) return -1
        var frame = 0
        for (i in 0 until 6) {
            HAL.setBits(OUTPUTPORTS.TXclk.mask)
            HAL.clrBits(OUTPUTPORTS.TXclk.mask)
            val TXD = if (HAL.isBit(INPUTPORTS.TXD.mask)) 1 else 0
            frame = frame or (TXD shl i)
        }
        HAL.setBits(OUTPUTPORTS.TXclk.mask)
        HAL.clrBits(OUTPUTPORTS.TXclk.mask)
        val start = frame and 0b000001
        val stop = (frame and 0b100000).shr(5)
        val key = frame and 0b011110
        if (start == 0) return -1
        if (stop == 1) return -1
        else return (key shr 1) and 0b1111
    }
}
//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    KeyReceiver.init()
    TUI.init()
    println("■ Make sure to not spam keys, ring buffer will store them all")
    println("■ Insert sleep functions on serial receiver to see the protocol work")
    print("■ Initializing⬝")
    Time.sleep(1000)
    print("⬝")
    Time.sleep(1000)
    print("⬝\n")
    Time.sleep(1000)
    println("=============================================================================")
    while (true) {
        Time.sleep(2000)
        println("■ Press a key")
        Time.sleep(5000)
        val keyCode = KeyReceiver.serialReceiver()
        if (keyCode == -1) {
            println("Code: -1 | None")
            TUI.write("Code: -1 | None", 0, 0, true, true)
        }
        else {
            println("Code: $keyCode | ${keyCode.toString(2).padStart(4, '0')}")
            TUI.write("Code: $keyCode | ${keyCode.toString(2).padStart(4, '0')}", 0, 0, true, true)
        }
    }
}