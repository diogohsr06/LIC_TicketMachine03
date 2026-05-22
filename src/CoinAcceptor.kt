import isel.leic.utils.Time
/**Coin Acceptor**/
object CoinAcceptor {
    fun init() {
        HAL.init()
    }
    fun getCoinId(): Int {
        val id0 = if (HAL.isBit(INPUTPORTS.COIN_ID0.mask)) 1 else 0
        val id1 = if (HAL.isBit(INPUTPORTS.COIN_ID1.mask)) 1 else 0
        val id2 = if (HAL.isBit(INPUTPORTS.COIN_ID2.mask)) 1 else 0
        return id0 or (id1 shl 1) or (id2 shl 2)
    }
    fun coinValue(): Int? {
        val value = when (getCoinId()) {
            0 -> 5
            1 -> 10
            2 -> 20
            3 -> 50
            4 -> 100
            5 -> 200
            else -> null
        }
        return value
    }
    fun coinInserted() = HAL.isBit(INPUTPORTS.COIN.mask)
    fun coinAccept() {
        if (!coinInserted()) return
        HAL.setBits(OUTPUTPORTS.ACCEPT.mask)
        while (HAL.isBit(INPUTPORTS.COIN.mask)) {Time.sleep(1)}
        HAL.clrBits(OUTPUTPORTS.ACCEPT.mask)
    }
    fun coinCollect() {
        HAL.setBits(OUTPUTPORTS.COLLECT.mask)
        Time.sleep(2000)
        HAL.clrBits(OUTPUTPORTS.COLLECT.mask)
    }
    fun coinReturn() {
        HAL.setBits(OUTPUTPORTS.EJECT.mask)
        Time.sleep(2000)
        HAL.clrBits(OUTPUTPORTS.EJECT.mask)
    }
}

/**Teste**/
fun main() {
    CoinAcceptor.init()
    println("Initialising")
    Time.sleep(2000)
    while (!CoinAcceptor.coinInserted()) {
        println("Insert a coin")
        Time.sleep(5000)
        println("Coin?: ${CoinAcceptor.coinInserted()}")
        Time.sleep(1000)
    }
    println("ID: ${CoinAcceptor.getCoinId()}")
    println("Coin value: ${Others.centsToEuros(CoinAcceptor.coinValue()!!)}")
    Time.sleep(2000)
    println("Accepting coin")
    CoinAcceptor.coinAccept()
    Time.sleep(2000)
    println("Collecting coin")
    CoinAcceptor.coinCollect()
    Time.sleep(2000)
    println("Ejecting coin")
    CoinAcceptor.coinReturn()
    Time.sleep(2000)
    println("Test done!")
}