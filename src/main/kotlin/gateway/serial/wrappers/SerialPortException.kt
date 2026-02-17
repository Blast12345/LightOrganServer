package gateway.serial.wrappers

import annotations.Wrapper

@Wrapper
class SerialPortException(
    portPath: String,
    message: String
) : Exception("$portPath: $message")
