import isel.leic.utils.Time

/**Liquid Cristal Display**/
object LCD {
    /**Dimensão do display**/
    const val LINES = 2
    const val COLS = 16
    /**Escreve um Byte de comando/dados no LCD em série**/
    private fun writeByteSerial(rs: Boolean, data: Int) {
        val rsBit = if (rs) 1 else 0
        val frame1 = rsBit or (data shl 1) or (1 shl 9)
        val frame2 = rsBit or (data shl 1)
        SerialEmitter.send(SerialEmitter.Peripherial.LCD, frame1)
        SerialEmitter.send(SerialEmitter.Peripherial.LCD, frame2)
    }
    /**Escreve um Byte de comando/dados no LCD**/
    private fun writeByte(rs: Boolean, data: Int) {
        writeByteSerial(rs, data)
    }
    /**Escreve um comando no LCD**/
    private fun writeCMD(data: Int) {
        writeByte(false, data)
    }
    /**Escreve um dado**/
    private fun writeDATA(data: Int) {
        writeByte(true, data)
    }
    /**Envia a sequência de iniciação para comunicação a 8 bits**/
    fun init() {
        writeCMD(0b0011_0000)
        Time.sleep(20)
        writeCMD(0b0011_0000)
        Time.sleep(20)
        writeCMD(0b0011_0000)
        writeCMD(0b0011_1000)
        writeCMD(0b0000_1000)
        writeCMD(0b0000_0001)
        writeCMD(0b0000_0110)
        writeCMD(0b0000_1111)
    }
    /**Escreve um carater na posição corrente**/
    fun write(c: Char) {
        writeDATA(c.code)
    }
    /**Escreve uma string na posição corrente**/
    fun write(text: String) {
        for (texto in text)
            write(texto)
    }
    /**Envia comando para posicionar cursor('line':0..LINES-1, 'column':0..COLS-1)**/
    fun cursor(line: Int, column: Int) {
        writeCMD((line * 0x40 + column) or 0x80)
    }
    /**Envia comando para limpar o ecrã e posicionar o cursor em (0,0)**/
    fun clear() {
        writeCMD(0x01)
        cursor(0, 0)
    }
}
/**Teste**/
fun main() {
    LCD.init()
    Time.sleep(2000)
    while(true) {
        LCD.clear()
        LCD.cursor(0, 0)
        LCD.write("Texto")
        LCD.cursor(1,0)
        LCD.write('G')
        Time.sleep(5000)
    }
}

