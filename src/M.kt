import isel.leic.utils.Time
/**Maintenance**/
object M {
    fun enabled() = HAL.isBit(INPUTPORTS.M_OUT.mask)
    fun init() {
        HAL.init()
    }
}

/**Teste**/
fun main() {
    M.init()
    println("M state: ${M.enabled()}")
    Time.sleep(1000)
    println("Enable M in hardware")
    Time.sleep(5000)
    println("M state: ${M.enabled()}")
}