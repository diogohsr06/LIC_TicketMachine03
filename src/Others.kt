import java.text.DecimalFormat
import isel.leic.utils.Time

//======================================================================================================================
//                                                      Others
//======================================================================================================================
/**Utilities**/
object Utils {
    private val euroFormat = DecimalFormat("0.00")
    fun centsToEuros(value: Int): String {
        return euroFormat.format(value / 100.0)
    }
    fun readDigitIdx(firstDigit: Char, maxSize: Int): Int {
        var value = firstDigit.digitToInt()
        while (true) {
            val next = KBD.waitKey(5000L)
            if (next !in '0'..'9') break
            value = value * 10 + next.digitToInt()
            if (value >= maxSize) value %= maxSize
        }
        return value.coerceIn(0, maxSize - 1)
    }
}
/**Miscellaneous Features**/
object Miscellaneous {
    const val RESET = "\u001B[0m"
    const val GREEN = "\u001B[32m"
    const val YELLOW = "\u001B[33m"
    const val CYAN = "\u001B[36m"
    const val RED = "\u001B[31m"
    const val BOLD = "\u001B[1m"
    fun writeScrollingText(text: String, row: Int, scrollIndex: Int, width: Int = 16) {
        if (text.length <= width) {
            TUI.write(text.padEnd(width), row, 0, false, false)
        } else {
            val extendedText = "$text      $text"
            val offset = scrollIndex % (text.length + 6)
            val sliced = extendedText.substring(offset, offset + width)
            TUI.write(sliced, row, 0, false, false)
        }
    }
}