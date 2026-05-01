/**Ticket Dispenser**/
object TicketDispenser {
    /**inicia a classe, estabelecendo os valores iniciais**/
    fun init() {
        SerialEmitter.init()
    }
    /**Envia comando para dispensar um bilhete**/
    fun activatePrintingTicket(roundTrip: Boolean, origin: Int, destination: Int) {
        val RT = if (roundTrip) 1 else 0
        val data = RT or destination.shl(1) or origin.shl(5)
        val printData = RT or destination.shl(1) or origin.shl(5) or (1 shl 9)
        SerialEmitter.send(SerialEmitter.Peripherial.TICKET, data)
        SerialEmitter.send(SerialEmitter.Peripherial.TICKET, printData)
    }
}

/**Teste**/
fun main() {
    TicketDispenser.init()
    TicketDispenser.activatePrintingTicket(true, 3, 6)
}