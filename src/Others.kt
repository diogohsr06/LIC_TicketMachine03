import java.text.DecimalFormat

//======================================================================================================================
//                                                      Utilities
//======================================================================================================================
object Others {
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