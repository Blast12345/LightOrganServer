import org.junit.*
import org.junit.Assert.*

class SampleTests {

    @Test
    fun `getName returns Alex Larson`() {
        val uut = Sample()
        val actual = uut.whatIsMyName()
        val expected = "Alex Larson"
        assertEquals(expected, actual)
    }

}