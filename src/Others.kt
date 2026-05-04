import java.text.DecimalFormat

/**Others**/
object Others {
    private val euroFormat = DecimalFormat("0.00")
    fun centsToEuros(value: Int): String {
        return euroFormat.format(value / 100.0)
    }
}

fun main() {
    println(Others.centsToEuros(250))
}