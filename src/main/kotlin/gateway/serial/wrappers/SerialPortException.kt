package gateway.serial.wrappers

import wrappers.Wrapper

@Wrapper
class SerialPortException(
    portPath: String,
    message: String
) : Exception("$portPath: $message")
