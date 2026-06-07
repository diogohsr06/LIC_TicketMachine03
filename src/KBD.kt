import isel.leic.UsbPort
import isel.leic.utils.Time

//======================================================================================================================
//                                                      Keyboard
//======================================================================================================================
object KBD {
    /**NONE refers to invalid key.
     *
     * none converts to a null character (\u0000), since 0 is a valid key code.**/
    const val NONE = 0;
    val none = NONE.toChar()
    /**
     * Function: init()
     *
     * Description: This functions inits the object
     * @param void
     * @return void
     * @see KeyReceiver.init
     */
    fun init() {
        KeyReceiver.init()
    }
    /**
     * Function: getKey()
     *
     * Description: Instantly decodes and gives corresponding char.
     * @param void
     * @return keyConvert. This returns the corresponding char of the key code.
     * @see KeyReceiver.serialReceiver
     */
    fun getKey(): Char {
        val keyCode = KeyReceiver.serialReceiver()
        val keyConvert =
            when (keyCode) {
                0 -> '1'
                1 -> '4'
                2 -> '7'
                3 -> '*'
                4 -> '2'
                5 -> '5'
                6 -> '8'
                7 -> '0'
                8 -> '3'
                9 -> '6'
                10 -> '9'
                11 -> '#'
                12 -> 'A'
                13 -> 'B'
                14 -> 'C'
                15 -> 'D'
                else -> none
            }
        return keyConvert
    }
    /**
     * Function: waitKey()
     *
     * Description: Returns key if pressed before timeout,
     * otherwise returns none.
     * @param timeout Refers to duration, in milliseconds, of a key detection.
     * @return Key or none.
     * @see KBD.getKey
     * @see System.currentTimeMillis
     */
    fun waitKey(timeout: Long): Char {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeout) {
            val key = getKey()
            if (key != none) {
                return key
            }
        }
        return none
    }
}
//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    KBD.init()
    println("■ To test Ring buffer: Turn software off and press keys")
    println("■ Test different repeat intervals with Switches 5 & 6")
    print("■ Initializing⬝")
    Time.sleep(1000)
    print("⬝")
    Time.sleep(1000)
    print("⬝\n")
    Time.sleep(1000)
    println("=============================================================================")
    print("Keys pressed: ")
    while(true) {
        print("${KBD.waitKey(100000)}, ")
    }
}
