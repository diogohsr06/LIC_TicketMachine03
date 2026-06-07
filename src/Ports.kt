//======================================================================================================================
//                                            USBPORT INPUTS & OUTPUTS
//======================================================================================================================
/**
 * Class: OUTPUTPORTS()
 *
 * Description: This enumerated class stores the masks
 * of the outports
 * @param mask Corresponding bit
 */
enum class OUTPUTPORTS(val mask: Int) {
    SDX(0b00000001),
    SCLK(0b00000010),
    SS_LCD(0b00000100),
    SS_TD(0b00001000),
    ACCEPT(0b00010000),
    EJECT(0b00100000),
    COLLECT(0b01000000),
    TXclk(0b10000000),
}
/**
 * Class: INPUTPORTS()
 *
 * Description: This enumerated class stores the masks
 * of the inports
 * @param mask Corresponding bit
 */
enum class INPUTPORTS(val mask: Int) {
    COIN_ID0(0b00000001),
    COIN_ID1(0b00000010),
    COIN_ID2(0b00000100),
    COIN(0b00001000),
    FN(0b00010000),
    M_OUT(0b01000000),
    TXD(0b10000000),
}