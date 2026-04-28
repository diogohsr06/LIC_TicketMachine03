/**Keyboard**/
object KBD {
    const val NONE = 0;
    val none = NONE.digitToChar()
    /**Inicia a classe**/
    fun init() {
        KeyReceiver.init()
    }
    /**Retorna de imediato a tecla premida ou NONE se não há tecla premida**/
    fun getKey(): Char {
        val keyCode = HAL.readBits(0x0F)
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
    while(true) {
        if(HAL.isBit(0b00010000)){
            KBD.waitKey(10)
            Thread.sleep(1)
            HAL.writeBits(0xFF,0b00000001)
        }
        else HAL.clrBits(0xFF)
    }
}
