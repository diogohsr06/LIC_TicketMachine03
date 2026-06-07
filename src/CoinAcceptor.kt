import isel.leic.utils.Time

//======================================================================================================================
//                                                  COIN ACCEPTOR
//======================================================================================================================
object CoinAcceptor {
    /**
     * Function: init()
     *
     * Description: This functions inits the object
     * @param void
     * @return void
     * @see HAL.isBit
     */
    fun init() {
        HAL.init()
    }
    /**
     * Function: getCoinId()
     *
     * Description: Reads CoinID input bits
     * @param void
     * @return id0 or (id1 shl 1) or (id2 shl 2) - Frame with full coin id
     * @see HAL.isBit
     */
    fun getCoinId(): Int {
        val id0 = if (HAL.isBit(INPUTPORTS.COIN_ID0.mask)) 1 else 0
        val id1 = if (HAL.isBit(INPUTPORTS.COIN_ID1.mask)) 1 else 0
        val id2 = if (HAL.isBit(INPUTPORTS.COIN_ID2.mask)) 1 else 0
        return id0 or (id1 shl 1) or (id2 shl 2)
    }
    /**
     * Function: coinValue()
     *
     * Description: Gets coin value from ID
     * @param void
     * @return Coin value associated with id or null when id is invalid
     * @see getCoinId
     */
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
    /**
     * Function: coinInserted()
     *
     * Description: Checks if a coin has been inserted
     * @param void
     * @return HAL.isBit(INPUTPORTS.COIN.mask) - Logic value of Coin
     * @see HAL.isBit
     */
    fun coinInserted() = HAL.isBit(INPUTPORTS.COIN.mask)
    /**
     * Function: coinAccept()
     *
     * Description: Accepts the coin inserted
     * @param void
     * @return void
     * @see HAL.setBits
     * @see HAL.clrBits
     */
    fun coinAccept() {
        if (!coinInserted()) return
        HAL.setBits(OUTPUTPORTS.ACCEPT.mask)
        while (HAL.isBit(INPUTPORTS.COIN.mask)) {Time.sleep(1)}
        HAL.clrBits(OUTPUTPORTS.ACCEPT.mask)
    }
    /**
     * Function: coinCollect()
     *
     * Description: Collects the coin
     * @param void
     * @return void
     * @see HAL.setBits
     * @see HAL.clrBits
     */
    fun coinCollect() {
        HAL.setBits(OUTPUTPORTS.COLLECT.mask)
        Time.sleep(2000)
        HAL.clrBits(OUTPUTPORTS.COLLECT.mask)
    }
    /**
     * Function: coinReturn()
     *
     * Description: Ejects the coin
     * @param void
     * @return void
     * @see HAL.setBits
     * @see HAL.clrBits
     */
    /**Ejects the coin**/
    fun coinReturn() {
        HAL.setBits(OUTPUTPORTS.EJECT.mask)
        Time.sleep(2000)
        HAL.clrBits(OUTPUTPORTS.EJECT.mask)
    }
}

//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    CoinAcceptor.init()
    println("■ Requires FPGA")
    println("■ To select a coin, use Switches 2..0")
    println("■ To insert a coin, use Switch 3\n")
    print("■ Initializing⬝")
    Time.sleep(1000)
    print("⬝")
    Time.sleep(1000)
    print("⬝\n")
    Time.sleep(1000)
    println("=============================================================================")
    println("■ Choose a task:")
    println("0 - Accept")
    println("1 - Collect")
    println("2 - Eject")
    println("3 - Quit")
    println("======================================")
    var quit = false
    while (!quit) {
        val isInserted = CoinAcceptor.coinInserted()
        val currentId = if (isInserted) CoinAcceptor.getCoinId() else "None"
        val currentValue = CoinAcceptor.coinValue()?.let { Others.centsToEuros(it) } ?: "N/A"
        print("\r■ Status: Inserted: $isInserted | ID: $currentId | Value: $currentValue\n")
        print("\r> ")
        val key = readln().toInt()
            when (key) {
                0 -> {
                    if (isInserted) {
                        println("■ Accepting coin...")
                        CoinAcceptor.coinAccept()
                    } else {
                        println("■ No coin detected to accept!")
                    }
                }
                1 -> {
                    println("■ Collecting coin...")
                    CoinAcceptor.coinCollect()
                }
                2 -> {
                    println("■ Ejecting coin...")
                    CoinAcceptor.coinReturn()
                }
                3 -> {
                    quit = true
                    println("■ Quitting...")
                }
            }
        }
    Time.sleep(100)
}