import isel.leic.utils.Time

//======================================================================================================================
//                                                  COIN DEPOSIT
//======================================================================================================================
object CoinDeposit {
    private var deposited = 0
    private var coins = arrayOf<FileAccess.Coins>()
    private var currCoins = IntArray(6)
    /**Inits the object**/
    fun init() {
        coins = FileAccess.readCoins("CoinDeposit.txt")
    }
    /**Inserts coin on deposit**/
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
    /**Aborts & returns coins**/
    fun exchange(value: Int): Boolean {
        if (deposited < value) return false
        for (i in currCoins.indices) {
            coins[i].amount += currCoins[i]
        }
        currCoins = IntArray(6)
        deposited = 0
        return true
    }
    /**Aborts & returns**/
    fun cancel() {
        deposited = 0
        currCoins = IntArray(6)
    }
    /**Returns total deposited**/
    fun getTotal() = deposited
    /**Returns coin amount**/
    fun getCount(id: Int) = coins[id].amount
    /**Resets counters**/
    fun resetCnt() {
        for (i in coins.indices) {
            coins[i].amount = 0
        }
    }
    /**Saves coins**/
    fun saveCoins() {
        FileAccess.writeCoins("CoinDeposit.txt", coins)
    }
}

//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    CoinDeposit.init()
    println("■ Initializing...")
    Time.sleep(3000)
    println("=============================================================================")
    println("■ Available Coins (IDs): 0:5c,\n1:10c,\n2:20c,\n3:50c,\n4:1€,\n5:2€")
    println("===========================================")
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
        print("\r■ Current Deposit: ${CoinDeposit.getTotal()}c\n")
        print("\r> ")
        val key = readln().toInt()
            when (key) {
                in 0..5 -> {
                    val id = key
                    CoinDeposit.insert(id)
                    println("■ Inserted coin ID: $id. New total: ${CoinDeposit.getTotal()}c")
                }
                6 -> {
                    print("■ Amount to exchange for? ")
                    val amount = readln().toIntOrNull() ?: 0
                    if (CoinDeposit.exchange(amount)) println("■ Exchange successful!")
                    else println("■ Exchange failed! Deposit insufficient.")
                }
                7 -> {
                    CoinDeposit.cancel()
                    println("■ Task cancelled. Deposit reset.")
                }
                8 -> {
                    CoinDeposit.saveCoins()
                    println("■ Coins saved to file.")
                }
                9 -> {
                    CoinDeposit.resetCnt()
                    println("■ Counters reset.")
                }
                10 -> {
                    quit = true
                    println("■ Quitting...")
                }
            }
    }
    Time.sleep(100)
}