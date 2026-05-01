import isel.leic.utils.Time
import java.util.Date
import java.text.SimpleDateFormat

/**Text User Interface**/
object TUI {
    val date = SimpleDateFormat("dd/MM/yyyy hh:mm").format(Date())
    fun init() {
        LCD.init()
        KBD.init()
    }
    fun startMenu() {
        LCD.write(" Ticket To Ride")
        LCD.cursor(1,0)
        LCD.write("$date")
    }
    fun writeKeyOnLCD() {
        LCD.write(KBD.waitKey(1000))
    }
    fun yesOrNo(text: String, time: Long): Boolean {
        LCD.clear()
        LCD.write(text)
        LCD.cursor(1,0)
        LCD.write("*-Yes  other-No")
        return KBD.waitKey(time) == '*'
    }
}

/**Teste**/
fun main() {
    TUI.init()
    TUI.startMenu()
    Time.sleep(5000)
    LCD.clear()

    LCD.write("Test1: Press Key:")
    LCD.cursor(1,0)
    Time.sleep(1000)
    TUI.writeKeyOnLCD()
    Time.sleep(5000)
    LCD.clear()

    LCD.write("Test2: Yes/No")
    Time.sleep(1000)
    val text = "LEIC melhor curso"
    LCD.clear()
    val yN = TUI.yesOrNo(text,5000)
    if (yN) {
        LCD.clear()
        LCD.write("SUIIIIII")
    } else {
        LCD.clear()
        LCD.write("67")
    }
    Time.sleep(5000)
    LCD.clear()

    LCD.write("Test3: Printing Tickets")
}
