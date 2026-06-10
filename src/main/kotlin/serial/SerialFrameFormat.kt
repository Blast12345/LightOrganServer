package serial

// REFERENCE: https://www.fernhillsoftware.com/help/drivers/serial-communication/index.html
data class SerialFrameFormat(
    val dataBits: DataBits,
    val parity: Parity,
    val stopBits: StopBits
) {

    enum class DataBits(val code: Char) { FIVE('5'), SIX('6'), SEVEN('7'), EIGHT('8') }
    enum class Parity(val code: Char) { NONE('N'), ODD('O'), EVEN('E'), MARK('M'), SPACE('S') }
    enum class StopBits(val code: Char) { ONE('1'), TWO('2') }

    companion object {
        val FORMAT_8N1 = SerialFrameFormat(DataBits.EIGHT, Parity.NONE, StopBits.ONE)
    }

    val notation: String = "${dataBits.code}${parity.code}${stopBits.code}"

}