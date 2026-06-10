import isel.leic.utils.Time
//======================================================================================================================
//                                                          M
//======================================================================================================================
object M {
    /**
     * Function: enabled()
     *
     * Description: Returns the logic value of the maintenance key
     * @param void
     * @return Boolean - Logic value of the bit of interest
     * @see HAL.isBit
     */
    fun enabled() = HAL.isBit(INPUTPORTS.M_OUT.mask)
    /**
     * Function: init()
     *
     * Description: This functions inits the object
     * @param void
     * @return void
     * @see HAL.init
     */
    fun init() {
        HAL.init()
    }
}
//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    M.init()
    TUI.init()
    println("${Miscellaneous.YELLOW}${Miscellaneous.BOLD}■ Press the M button")
    println("${Miscellaneous.YELLOW}${Miscellaneous.BOLD}■ On FPGA, use Switch 4")
    print("${Miscellaneous.CYAN}${Miscellaneous.BOLD}■ Initializing⬝")
    Time.sleep(1000)
    print("⬝")
    Time.sleep(1000)
    print("⬝\n")
    Time.sleep(1000)
    println("=============================================================================")
    var prev = M.enabled()
    while (true) {
        val curr = M.enabled()
        val state = if (M.enabled()) "ON" else "OFF"
        val stateColor = if (M.enabled()) Miscellaneous.GREEN else Miscellaneous.RED
        if (M.enabled() != prev) {
            print("\r$stateColor■ M State: $state")
            TUI.write("M State: $state", 0, 0, true, true)
            prev = curr
        }
        Time.sleep(10)
    }
}