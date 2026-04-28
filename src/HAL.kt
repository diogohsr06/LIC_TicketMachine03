import isel.leic.UsbPort

/**Hardware Abstract Layer**/
object HAL {
    private var usbport = 0
    /**Inicia o objeto**/
    fun init() {
        usbport = 0
        UsbPort.write(usbport)
    }
    /**Retorna os valores dos bits respresentados por mask presentes no UsbPort**/
    fun readBits(mask: Int): Int {
        return UsbPort.read() and mask
    }
    /**Retorna ’true’ se o bit definido pela mask esta com o valor logico ’1’ no UsbPort**/
    fun isBit(mask: Int): Boolean {
        return (UsbPort.read() and mask != 0)
    }
    /**Coloca os bits representados por mask no valor lógico '1'**/
    fun setBits(mask: Int) {
        usbport = usbport or mask
        UsbPort.write(usbport)
    }
    /**Coloca os bits representados por mask no valor lógico '0'**/
    fun clrBits(mask: Int) {
        usbport = usbport and mask.inv()
        UsbPort.write(usbport)
    }
    /**Escreve nos bits representados por mask os valores dos bits correspondentes em value**/
    fun writeBits(mask: Int, value: Int) {
        usbport = (usbport and mask.inv()) or (value and mask)
        UsbPort.write(usbport)
    }
}

/**Teste**/
fun main() {
    HAL.init()
    UsbPort.write(0b01101110)

    println(HAL.readBits(0b00001111))
    Thread.sleep(2000)

    println(HAL.isBit(0b00000010))
    Thread.sleep(2000)

    HAL.setBits(0b00001111)
    println(UsbPort.read())
    Thread.sleep(2000)

    HAL.clrBits(0b11110000)
    println(UsbPort.read())
    Thread.sleep(2000)

    HAL.writeBits(0b00001111, 0b11110000)
    println(UsbPort.read())
}