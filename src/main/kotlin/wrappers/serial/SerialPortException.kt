package wrappers.serial

class SerialPortException(
    portPath: String,
    message: String
) : Exception("$portPath: $message")
