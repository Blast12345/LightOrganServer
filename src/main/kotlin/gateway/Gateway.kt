package gateway

import lightOrgan.LightOrganSubscriber
import wrappers.color.Color

class Gateway(
    private val device: UsbDevice = UsbDeviceFinder.find(),
    private val colorMessageFactory: ColorMessageFactory = ColorMessageFactory()
): LightOrganSubscriber {

    init {
        if (port.openPort()) {
            println("Port opened successfully")
        } else {
            println("Failed to open port")
        }
    }

    override fun new(color: Color) {
        val colorMessage = colorMessageFactory.create(color)
        device.send(colorMessage)
    }

}