import isel.leic.utils.Time
//======================================================================================================================
//                                                          M
//======================================================================================================================
object M {
    /** Returns the logic value of the maintenance key**/
    fun enabled() = HAL.isBit(INPUTPORTS.M_OUT.mask)
    /**Inits the object**/
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
    println("■ Press the M button")
    println("■ On FPGA, use Switch 4")
    println("■ Initializing...")
    Time.sleep(3000)
    println("=============================================================================")
    var prev = M.enabled()
    while (true) {
        val curr = M.enabled()
        val state = if (M.enabled()) "True" else "False"
        if (M.enabled() != prev) {
            print("\r■ M State: $state")
            TUI.write("M State: $state", 0, 0, true, true)
            prev = curr
        }
        Time.sleep(10)
    }
}