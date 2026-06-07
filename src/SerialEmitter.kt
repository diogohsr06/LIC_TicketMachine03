import isel.leic.utils.Time

//======================================================================================================================
//                                                  SERIAL EMITTER
//======================================================================================================================
object SerialEmitter {
    enum class Peripherial {LCD, TICKET}
    /**Inicia a classe**/
    fun init() {
        HAL.init() //port a 0
        HAL.setBits(OUTPUTPORTS.SS_LCD.mask)
        HAL.setBits(OUTPUTPORTS.SS_TD.mask)
        HAL.clrBits(OUTPUTPORTS.SCLK.mask)
    }
    /**Envia tramas para os diferentes módulos SerialReceiver
    Identificando o periférico de destino em 'addr'
    os bits de dados em 'data'
    e em 'size' o número de bits a enviar **/
    fun send(addr: Peripherial, data: Int) {
        val SS = if (addr == Peripherial.LCD) OUTPUTPORTS.SS_LCD.mask else OUTPUTPORTS.SS_TD.mask
        HAL.clrBits(SS)
        for (i in 0 ..9) {
            HAL.clrBits(OUTPUTPORTS.SCLK.mask)
            val SDX = data shr i and 1
            if (SDX == 1) {
                HAL.setBits(OUTPUTPORTS.SDX.mask)
            } else HAL.clrBits(OUTPUTPORTS.SDX.mask)
            HAL.setBits(OUTPUTPORTS.SCLK.mask)
        }
        HAL.setBits(SS)
    }
}
//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    SerialEmitter.init()
    println("■ Write the data you want to be sent on Peripherals")
    println("■ Data is recommended to be written on 10 bits for better understanding")
    println("■ Initializing...")
    Time.sleep(3000)
    println("=============================================================================")
    while (true) {
        print("Data to LCD: ")
        val dataLCD = readln()
        val dataLCDbin = if (dataLCD.length == 10 && dataLCD.all { it == '0' || it == '1' }) dataLCD.toInt(2) else dataLCD.toInt()
        print("Data to TD: ")
        val dataTD = readln()
        val dataTDbin = if (dataTD.length == 10 && dataTD.all { it == '0' || it == '1' }) dataTD.toInt(2) else dataTD.toInt()
        SerialEmitter.send(SerialEmitter.Peripherial.LCD, dataLCDbin)
        SerialEmitter.send(SerialEmitter.Peripherial.TICKET, dataTDbin)
        print("■ Sent!\n")
        println("--------------------------------------------------------------------")
    }
}