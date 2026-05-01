import isel.leic.utils.Time

/**Keyboard**/
object KBD {
    const val NONE = 0;
    val none = NONE.toChar()
    /**Inicia a classe**/
    fun init() {
        KeyReceiver.init()
    }
    /**Retorna de imediato a tecla premida ou NONE se não há tecla premida**/
    fun getKey(): Char {
        val keyCode = KeyReceiver.serialReceiver()
        val keyConvert =
            when (keyCode) {
                0 -> '1'
                1 -> '4'
                2 -> '7'
                3 -> '*'
                4 -> '2'
                5 -> '5'
                6 -> '8'
                7 -> '0'
                8 -> '3'
                9 -> '6'
                10 -> '9'
                11 -> '#'
                12 -> 'A'
                13 -> 'B'
                14 -> 'C'
                15 -> 'D'
                else -> none
            }
        return keyConvert
    }
    /**Retorna a tecla premida, caso ocorra antes de 'timeout' (em milissegundos), ou NONE caso contrário**/
    fun waitKey(timeout: Long): Char {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeout) {
            val key = getKey()
            if (key != none) {
                return key
            }
        }
        return none
    }
}
/**Teste**/
fun main() {
    KBD.init()
    while(true) {
        println("Key: ${KBD.waitKey(10000)}")
    }
}
