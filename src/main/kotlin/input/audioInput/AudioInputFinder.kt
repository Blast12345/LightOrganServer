package input.audioInput

import input.audioDevice.AudioDeviceFinder

class AudioInputFinder(
    private val audioDeviceFinder: AudioDeviceFinder = AudioDeviceFinder()
) {

    fun findAll(): List<AudioInput> {
        val allDevices = audioDeviceFinder.findAll()
        return allDevices.flatMap { device -> device.inputs }
    }

    fun findDefaultInput(): AudioInput {
        return findAll().firstOrNull() ?: throw Exception("No audio input found.")
    }

}