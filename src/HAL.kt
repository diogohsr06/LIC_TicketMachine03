import isel.leic.UsbPort

//======================================================================================================================
//                                              HARDWARE ABSTRACT LAYER
//======================================================================================================================
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

//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    println("Teste1: Escrita")
    HAL.init()
    UsbPort.write(0b01101110)
    Thread.sleep(5000)

    println("Teste2: Leitura")
    println(HAL.readBits(0b00001111))
    Thread.sleep(5000)

    println("Teste3: isBit")
    println(HAL.isBit(0b00000010))
    Thread.sleep(5000)

    println("Teste4: Bits a 1")
    HAL.setBits(0b00001111)
    println(UsbPort.read())
    Thread.sleep(5000)

    println("Teste5: Bits a 0")
    HAL.clrBits(0b11110000)
    println(UsbPort.read())
    Thread.sleep(5000)

    println("Teste6: Escrita")
    HAL.writeBits(0b00001111, 0b11110000)
    println(UsbPort.read())
}