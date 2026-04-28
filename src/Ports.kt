/**USBPORT inputs & outputs**/
enum class OUTPUTPORTS(val mask: Int) {
    SDX(0b00000001),
    SCLK(0b00000010),
    SS_LCD(0b00000100),
    SS_TD(0b00001000),
    TXclk(0b10000000),
}

enum class INPUTPORTS(val mask: Int) {
    BUSY(0b00000001),
    FN(0b00010000),
    TXD(0b10000000),
}