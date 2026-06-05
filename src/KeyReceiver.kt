import isel.leic.utils.Time

object KeyReceiver {
    fun init() {
        HAL.init()
        HAL.clrBits(OUTPUTPORTS.TXclk.mask)
    }
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
/**Teste**/
fun main() {
    KeyReceiver.init()
    while (true) {
        Time.sleep(2000)
        println("Press a key")
        Time.sleep(5000)
        val keyCode = KeyReceiver.serialReceiver()
        println("Code: $keyCode")
    }
}