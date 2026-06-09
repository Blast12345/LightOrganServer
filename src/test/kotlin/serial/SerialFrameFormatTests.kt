package serial

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SerialFrameFormatTests {

    @Test
    fun `notation for 8N1 format`() {
        val format = SerialFrameFormat.FORMAT_8N1

        assertEquals("8N1", format.notation)
    }

    @Test
    fun `notation for 7E2 format`() {
        val format = SerialFrameFormat(
            SerialFrameFormat.DataBits.SEVEN,
            SerialFrameFormat.Parity.EVEN,
            SerialFrameFormat.StopBits.TWO
        )

        assertEquals("7E2", format.notation)
    }

}