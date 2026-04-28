/**Ticket Dispenser**/
object TicketDispenser {
    /**inicia a classe, estabelecendo os valores iniciais**/
    fun init() {
        SerialEmitter.init()
    }
    /**Envia comando para dispensar um bilhete**/
    fun activatePrintingTicket(roundTrip: Boolean, origin: Int, destination: Int) {
        val RT = if (roundTrip) 1 else 0
        val data = RT or (origin shl 1) or (destination shl 5)
        SerialEmitter.send(SerialEmitter.Peripherial.TICKET, data)
    }
}

/**Teste**/
fun main() {
    TicketDispenser.init()
    TicketDispenser.activatePrintingTicket(true, 3, 6)
}