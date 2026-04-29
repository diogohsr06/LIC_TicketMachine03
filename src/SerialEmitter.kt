import isel.leic.utils.Time

/**Serial Emitter**/
object SerialEmitter {
    enum class Peripherial {LCD, TICKET}
    /**Inicia a classe**/
    fun init() {
        HAL.init() //port a 0
        HAL.setBits(OUTPUTPORTS.SS_LCD.mask)
        HAL.setBits(OUTPUTPORTS.SS_TD.mask)
        HAL.clrBits(OUTPUTPORTS.SCLK.mask)
    }
    /**Envia tramas para os diferentes módulos SerialReceiver**/
    /**Identificando o periférico de destino em 'addr'**/
    /**os bits de dados em 'data'**/
    /**e em 'size' o número de bits a enviar**/
    fun send(addr: Peripherial, data: Int) {
        val SS = if (addr == Peripherial.LCD) OUTPUTPORTS.SS_LCD.mask else OUTPUTPORTS.SS_TD.mask
        HAL.clrBits(SS)
        for (i in 0 ..9) {
            HAL.clrBits(OUTPUTPORTS.SCLK.mask)
            val SDX = data shr i and 1
            if (SDX == 1) {
                HAL.setBits(OUTPUTPORTS.SDX.mask)
            } else HAL.clrBits(OUTPUTPORTS.SDX.mask)
            Time.sleep(5)
            HAL.setBits(OUTPUTPORTS.SCLK.mask)
        }
        HAL.setBits(SS)
    }
    /**Retorna informação se o periférico está ocupado**/
    fun isBusy(): Boolean = HAL.isBit(INPUTPORTS.BUSY.mask)
}
/**Teste**/
fun main() {
    SerialEmitter.init()
    SerialEmitter.send(SerialEmitter.Peripherial.LCD, 0b0010100101)
    SerialEmitter.send(SerialEmitter.Peripherial.TICKET, 0b1010100111)
}