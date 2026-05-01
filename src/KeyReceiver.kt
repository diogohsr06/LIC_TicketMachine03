import isel.leic.utils.Time

object KeyReceiver {
    fun init() {
        HAL.init()
        HAL.clrBits(OUTPUTPORTS.TXclk.mask)
    }
    fun serialReceiver(): Int {
        if (HAL.isBit(INPUTPORTS.TXD.mask)) return -1
        var data = 0
        for (i in 0 until 6) {
            HAL.setBits(OUTPUTPORTS.TXclk.mask)
            if (HAL.isBit(INPUTPORTS.TXD.mask)) {
                data = data or (1 shl i)
            }
            HAL.clrBits(OUTPUTPORTS.TXclk.mask)
        }
        HAL.setBits(OUTPUTPORTS.TXclk.mask)
        val STOP = HAL.isBit(INPUTPORTS.TXD.mask)
        HAL.clrBits(OUTPUTPORTS.TXclk.mask)
        return if (STOP) data else -1
    }
}
/**Teste**/
fun main() {
    KeyReceiver.init()
    Time.sleep(2000)
    println("Pressione uma tecla")
    Time.sleep(5000)
    val keyCode = KeyReceiver.serialReceiver()
    println("Código recebido: $keyCode")
}