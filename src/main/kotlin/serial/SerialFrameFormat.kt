package serial

// REFERENCE: https://www.fernhillsoftware.com/help/drivers/serial-communication/index.html
data class SerialFrameFormat(
    val dataBits: DataBits,
    val parity: Parity,
    val stopBits: StopBits
) {

    enum class DataBits { FIVE, SIX, SEVEN, EIGHT }
    enum class Parity { NONE, ODD, EVEN, MARK, SPACE }
    enum class StopBits { ONE, TWO }

    companion object {
        val FORMAT_8N1 = SerialFrameFormat(DataBits.EIGHT, Parity.NONE, StopBits.ONE)
    }

}