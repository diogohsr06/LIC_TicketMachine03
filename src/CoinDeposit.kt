import isel.leic.utils.Time

/**Coin Deposit**/
object CoinDeposit {
    var coins = arrayOf<FileAccess.Coins>()
    fun init() {
        coins = FileAccess.readCoins("CoinDeposit.txt")
    }
}