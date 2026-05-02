/**Coin Deposit**/
object CoinDeposit {
    fun init() {
        val coins = FileAccess.readCoins("CoinDeposit.txt")
    }
}