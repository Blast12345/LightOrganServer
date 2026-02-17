package audio.audioInput

class SampleBufferTests {

    private val bufferSize = 4
    private val newSamples1 = byteArrayOf(1)
    private val newSamples2 = byteArrayOf(2, 3)

    private fun createSUT(): SampleBuffer {
        return SampleBuffer(bufferSize)
    }

//    @Test
//    fun `the updated buffer is the same length as the initial size`() {
//        val initialSize = Random.Default.nextInt(1024)
//        val sut = SampleBuffer(initialSize)
//        val updatedSamples = sut.append(newSamples1)
//        Assertions.assertEquals(initialSize, updatedSamples.size)
//    }
//
//    @Test
//    fun `the updated buffer follows the first-in-first-out rule`() {
//        val sut = createSUT()
//
//        val updatesSamples1 = sut.append(newSamples1)
//        Assertions.assertArrayEquals(byteArrayOf(0, 0, 0, 1), updatesSamples1)
//
//        val updatesSamples2 = sut.append(newSamples2)
//        Assertions.assertArrayEquals(byteArrayOf(0, 1, 2, 3), updatesSamples2)
//    }

}