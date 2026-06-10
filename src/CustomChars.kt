//======================================================================================================================
//                                                    CUSTOM CHARS
//======================================================================================================================
/**Pattern for euro**/
val euro = intArrayOf(
    0b00110,
    0b01001,
    0b11100,
    0b01000,
    0b11100,
    0b01001,
    0b00110,
    0b00000
)
/**Pattern for up arrow**/
val upArrow = intArrayOf(
    0b00000,
    0b00100,
    0b01110,
    0b10101,
    0b00100,
    0b00100,
    0b00100,
    0b00000
)
/**Pattern for down arrow**/
val downArrow = intArrayOf(
    0b00000,
    0b00100,
    0b00100,
    0b00100,
    0b10101,
    0b01110,
    0b00100,
    0b00000
)
/**Full Block**/
val fullBlock = intArrayOf(
    0b00000,
    0b00000,
    0b11111,
    0b11111,
    0b11111,
    0b11111,
    0b00000,
    0b00000
)
/**Empty Block**/
val emptyBlock = intArrayOf(
    0b00000,
    0b00000,
    0b11111,
    0b10001,
    0b10001,
    0b11111,
    0b00000,
    0b00000
)
/**Up Triangle**/
val upTri = intArrayOf(
    0b00100,
    0b01110,
    0b01110,
    0b11111,
    0b11111,
    0b11111,
    0b11111,
    0b00000
)
/**Right Triangle**/
val rightTri = intArrayOf(
    0b10000,
    0b11000,
    0b11100,
    0b11110,
    0b11100,
    0b11000,
    0b10000,
    0b00000
)
/**Left Triangle**/
val leftTri = intArrayOf(
    0b00001,
    0b00011,
    0b00111,
    0b01111,
    0b00111,
    0b00011,
    0b00001,
    0b00000
)
/**Mini Triangle**/
val miniTri = intArrayOf(
    0b00000,
    0b10000,
    0b11000,
    0b11100,
    0b11000,
    0b10000,
    0b00000,
    0b00000
)