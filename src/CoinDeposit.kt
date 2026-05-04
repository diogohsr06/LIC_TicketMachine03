import isel.leic.utils.Time

/**Coin Deposit**/
object CoinDeposit {
    private var deposited = 0
    private var coins = arrayOf<FileAccess.Coins>()
    private var currCoins = IntArray(6)
    fun init() {
        coins = FileAccess.readCoins("CoinDeposit.txt")
    }
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
    fun exchange(value: Int): Boolean {
        if (deposited < value) return false
        for (i in currCoins.indices) {
            coins[i].amount += currCoins[i]
        }
        currCoins = IntArray(6)
        deposited = 0
        return true
    }
    fun cancel() {
        deposited = 0
        currCoins = IntArray(6)
    }
    fun getTotal() = deposited
    fun getCount(id: Int) = coins[id].amount
    fun resetCnt() {
        for (i in coins.indices) {
            coins[i].amount = 0
        }
    }
    fun saveCoins() {
        FileAccess.writeCoins("CoinDeposit.txt", coins)
    }
}

/**Teste**/
fun main() {
    CoinDeposit.init()
    println("Deposit: ${CoinDeposit.getTotal()}")
    println("Insert coinId: ")
    val inserted = readln().toInt()
    println("Wait...")
    Time.sleep(1000)
    println("Press s to cancel")
    val cancel = readln()
    if (cancel == "s") {
        CoinDeposit.cancel()
        println("Cancelled")
    }
    Time.sleep(5000)
    CoinDeposit.insert(inserted)
    println("Coin inserted!")
    println("Deposit: ${CoinDeposit.getTotal()}")
    Time.sleep(3000)
    println("Exchange: ")
    val exchange = readln().toInt()
    println("Wait...")
    CoinDeposit.exchange(exchange)
    Time.sleep(5000)
    println("Done!")
    println("Deposit: ${CoinDeposit.getTotal()}")
    println("Coin Count: ${CoinDeposit.getCount(inserted)}")
    println("Saving to file...")
    CoinDeposit.saveCoins()
    Time.sleep(5000)
    println("Done! Go Check File.")
}