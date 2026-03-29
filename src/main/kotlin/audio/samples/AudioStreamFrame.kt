package audio.samples

// NOTE: sequenceNumber and bufferWasFull are used to infer risk of samples being dropped.
// NOTE: Overflowing the sequence number is not a concern. At 1 frame per sample @ 48 kHz, it would take ~6 million years to overflow.
class AudioStreamFrame(
    val audio: AudioFrame,
    val sequenceNumber: Long,
    val bufferWasFull: Boolean
)