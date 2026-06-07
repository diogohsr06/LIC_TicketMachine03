import isel.leic.utils.Time
import kotlin.system.exitProcess

//======================================================================================================================
//                                                TICKET DISPENSER
//======================================================================================================================
object TicketDispenser {
    /**
     * Function: init()
     *
     * Description: This functions inits the object
     * @param void
     * @return void
     * @see SerialEmitter.init
     */
    fun init() {
        SerialEmitter.init()
    }
    /**
     * Function: activatePrintingTicket()
     *
     * Description: Sends a command to print a ticket.
     * @param roundTrip Trip type
     * @param origin Origin station
     * @param destination Destination station
     * @return void
     * @see SerialEmitter.send
     * @see shl
     */
    fun activatePrintingTicket(roundTrip: Boolean, origin: Int, destination: Int) {
        val RT = if (roundTrip) 1 else 0
        val data = RT or destination.shl(1) or origin.shl(5)
        val printData = RT or destination.shl(1) or origin.shl(5) or (1 shl 9)
        SerialEmitter.send(SerialEmitter.Peripherial.TICKET, data)
        SerialEmitter.send(SerialEmitter.Peripherial.TICKET, printData)
    }
    /**
     * Function: lowerPrt()
     *
     * Description: Sends a command with Prt to low. This prevents the system
     * from freezing when collecting a ticket.
     * @param roundTrip Trip type
     * @param origin Origin station
     * @param destination Destination station
     * @return void
     * @see SerialEmitter.send
     * @see shl
     */
    fun lowerPrt(roundTrip: Boolean, origin: Int, destination: Int) {
        val RT = if (roundTrip) 1 else 0
        val data = RT or destination.shl(1) or origin.shl(5)
        SerialEmitter.send(SerialEmitter.Peripherial.TICKET, data)
    }
}

//======================================================================================================================
//                                                      TESTBENCH
//======================================================================================================================
fun main() {
    TicketDispenser.init()
    println("■ Write the data you want to be sent")
    println("■ Data is recommended to be written on binary for better understanding")
    print("■ Initializing⬝")
    Time.sleep(1000)
    print("⬝")
    Time.sleep(1000)
    print("⬝\n")
    Time.sleep(1000)
    println("=============================================================================")
    while (true) {
        print("RoundTrip: ")
        val rt = readln().toBoolean()
        print("Origin: ")
        val origin = readln()
        print("Destination: ")
        val destination = readln()
        val originBin = if (origin.length == 4 && origin.all { it == '0' || it == '1' }) origin.toInt(2) else origin.toInt()
        val destinationBin = if (destination.length == 4 && destination.all { it == '0' || it == '1' }) destination.toInt(2) else destination.toInt()
        TicketDispenser.activatePrintingTicket(rt, originBin, destinationBin)
        println("■ Sending...")
        Time.sleep(3000)
        TicketDispenser.lowerPrt(false, 0, 0)
        print("Continue [y/n]? ")
        val next = readln()
        if (next.lowercase() != "y") exitProcess(0)
        println("--------------------------------------------------------------------")
    }
}