package wrappers.sound

import io.mockk.every
import io.mockk.mockk
import toolkit.monkeyTest.nextPositiveFloat
import toolkit.monkeyTest.nextPositiveInt
import java.util.concurrent.LinkedBlockingQueue
import javax.sound.sampled.TargetDataLine
import kotlin.random.Random.Default.nextBoolean

class FakeTargetDataLine(
    val dataLine: TargetDataLine = mockk(),
    val format: javax.sound.sampled.AudioFormat = mockk()
) {

    var sampleRate = nextPositiveFloat()
    var sampleSizeInBits = nextPositiveInt()
    var channels = nextPositiveInt()
    var isBigEndian = nextBoolean()

    private val buffer: LinkedBlockingQueue<Byte> = LinkedBlockingQueue()

    init {
        // Format
        every { dataLine.format } returns format
        every { format.sampleRate } answers { sampleRate }
        every { format.sampleSizeInBits } answers { sampleSizeInBits }
        every { format.channels } answers { channels }
        every { format.isBigEndian } answers { isBigEndian }
        every { format.frameSize } returns 1 // To keep things simple

        // Lifecycle
        every { dataLine.open(any(), any()) } returns Unit
        every { dataLine.start() } returns Unit
        every { dataLine.stop() } returns Unit
        every { dataLine.close() } returns Unit

        // Reading
        every { dataLine.available() } answers { buffer.size }
        every { dataLine.read(any(), any(), any()) } answers {
            val dest = firstArg<ByteArray>()
            val offset = secondArg<Int>()
            val length = thirdArg<Int>()

            dest[offset] = buffer.take()
            var bytesRead = 1
            while (bytesRead < length && buffer.isNotEmpty()) {
                dest[offset + bytesRead] = buffer.poll() ?: break
                bytesRead++
            }
            bytesRead
        }
    }

    fun queue(data: ByteArray) {
        data.forEach { buffer.put(it) }
    }

}