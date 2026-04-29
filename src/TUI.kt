/**On going and needs to be adapted with keyReceiver**/
/**Text User Interface**/
object TUI {
    fun init() {
        LCD.init()
        KBD.init()
    }
    fun writeKeyOnLCD() {
        LCD.write(KBD.waitKey(1000))
    }
}
