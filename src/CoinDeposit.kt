import isel.leic.utils.Time

//======================================================================================================================
//                                                  COIN DEPOSIT
//======================================================================================================================
object CoinDeposit {
    private var deposited = 0
    private var coins = arrayOf<FileAccess.Coins>()
    private var currCoins = IntArray(6)
    /**
     * Function: init()
     *
     * Description: This functions inits the object
     * @param void
     * @return void
     * @see FileAccess.readCoins
     */
    fun init() {
        coins = FileAccess.readCoins("CoinDeposit.txt")
    }
    /**
     * Function: insert()
     *
     * Description: Inserts coin on deposit
     * @param id Coin identification
     * @return void
     */
    fun insert(id: Int) {
        val value = when (id) {
            0 -> 5
            1 -> 10
            2 -> 20
            3 -> 50
            4 -> 100
            5 -> 200
            else -> 0
        }
        if (value > 0) {
            deposited += value
            currCoins[id]++
        }
    }
    /**
     * Function: exchange()
     *
     * Description: Aborts & returns coins
     * @param value Value to be exchanged
     * @return Boolean - Mainly to check if deposit has enough to exchange
     */
    fun exchange(value: Int): Boolean {
        if (deposited < value) return false
        for (i in currCoins.indices) {
            coins[i].amount += currCoins[i]
        }
        currCoins = IntArray(6)
        deposited = 0
        return true
    }
    /**
     * Function: cancel()
     *
     * Description: Aborts & returns
     * @param void
     * @return void
     */
    fun cancel() {
        deposited = 0
        currCoins = IntArray(6)
    }
    /**
     * Function: getTotal()
     *
     * Description: Returns total deposited
     * @param void
     * @return deposited - Value on the deposit
     */
    fun getTotal() = deposited
    /**
     * Function: getCount()
     *
     * Description: Returns coin amount
     * @param id Coin identification
     * @return coins[id].amount - Amount of the coin type
     */
    fun getCount(id: Int) = coins[id].amount
    /**
     * Function: resetCnt()
     *
     * Description: Resets counters
     * @param void
     * @return void
     */
    fun resetCnt() {
        for (i in coins.indices) {
            coins[i].amount = 0
        }
    }
    /**
     * Function: saveCoins()
     *
     * Description: Saves coins
     * @param void
     * @return void
     */
    fun saveCoins() {
        FileAccess.writeCoins("CoinDeposit.txt", coins)
    }
}

//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    CoinDeposit.init()
    print("${Miscellaneous.CYAN}${Miscellaneous.BOLD}■ Initializing⬝")
    Time.sleep(1000)
    print("⬝")
    Time.sleep(1000)
    print("⬝\n")
    Time.sleep(1000)
    println("=============================================================================")
    println("${Miscellaneous.GREEN}${Miscellaneous.BOLD}■ Available Coins (IDs): 0:5c,\n1:10c,\n2:20c,\n3:50c,\n4:1€,\n5:2€")
    println("${Miscellaneous.YELLOW}${Miscellaneous.BOLD}===========================================")
    println("■ Choose a task:")
    println("0..5 - Insert Coin")
    println("6 - Exchange")
    println("7 - Cancel")
    println("8 - Save")
    println("9 - Reset Counters")
    println("10 - Quit")
    println("======================================")
    var quit = false
    while (!quit) {
        print("\r${Miscellaneous.GREEN}${Miscellaneous.BOLD}■ Current Deposit: ${CoinDeposit.getTotal()}c\n")
        print("\r${Miscellaneous.GREEN}${Miscellaneous.BOLD}> ")
        val key = readln().toInt()
            when (key) {
                in 0..5 -> {
                    val id = key
                    CoinDeposit.insert(id)
                    println("${Miscellaneous.YELLOW}${Miscellaneous.BOLD}■ Inserted coin ID: $id. New total: ${CoinDeposit.getTotal()}c")
                }
                6 -> {
                    print("${Miscellaneous.YELLOW}${Miscellaneous.BOLD}■ Amount to exchange for? ")
                    val amount = readln().toIntOrNull() ?: 0
                    if (CoinDeposit.exchange(amount)) println("${Miscellaneous.GREEN}${Miscellaneous.BOLD}■ Exchange successful!")
                    else println("${Miscellaneous.RED}${Miscellaneous.BOLD}■ Exchange failed! Deposit insufficient.")
                }
                7 -> {
                    CoinDeposit.cancel()
                    println("${Miscellaneous.GREEN}${Miscellaneous.BOLD}■ Task cancelled. Deposit reset.")
                }
                8 -> {
                    CoinDeposit.saveCoins()
                    println("${Miscellaneous.GREEN}${Miscellaneous.BOLD}■ Coins saved to file.")
                }
                9 -> {
                    CoinDeposit.resetCnt()
                    println("${Miscellaneous.GREEN}${Miscellaneous.BOLD}■ Counters reset.")
                }
                10 -> {
                    quit = true
                    println("${Miscellaneous.CYAN}${Miscellaneous.BOLD}■ Quitting...")
                }
            }
    }
    Time.sleep(100)
}