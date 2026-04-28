import isel.leic.utils.Time

/**Key Receiver**/
object KeyReceiver {
    fun init() {
        HAL.init()
        HAL.clrBits(OUTPUTPORTS.TXclk.mask)
    }
    fun serialReceiver(): Int {
        val TXD = if (HAL.isBit(INPUTPORTS.TXD.mask)) 1 else 0
        if (HAL.isBit(INPUTPORTS.TXD.mask)) return -1
        var frame = 0
        for (i in 0..6) {
            HAL.setBits(OUTPUTPORTS.TXclk.mask)
            HAL.clrBits(OUTPUTPORTS.TXclk.mask)
            frame = frame or (TXD shl i)
        }
        HAL.setBits(OUTPUTPORTS.TXclk.mask)
        HAL.clrBits(OUTPUTPORTS.TXclk.mask)
        val START = frame and 0b100000
        val STOP = frame and 0b000001
        val D = frame and 0b011110
        if (START == 0) return -1
        else if (STOP == 1) return -1
        else return (D shr 1) and 0b1111
    }
}

/**Teste**/
/**Needs manipulation of TXd & key pressing on hardware for testing. Order:
 * TXd -> 0;
 * TXd -> 1;
 * Press key;
 * TXd -> 0;**/
fun main() {
    KeyReceiver.init()
    Time.sleep(5000)
    println(KeyReceiver.serialReceiver())
}