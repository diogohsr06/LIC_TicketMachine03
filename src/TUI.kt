/**On going and needs to be adapted with keyReceiver**/
/**Text User Interface**/
object TUI {
    fun init() {
        LCD.init()
        KBD.init()
    }
    fun writeKeyOnLCD() {
        LCD.clear()
        while (true) {
            if (HAL.isBit(0b00010000)) {
                val key = KBD.waitKey(10)
                Thread.sleep(1)
                if (key != KBD.none) {
                    LCD.write(key)
                }
            }
        }
    }
}
