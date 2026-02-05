package gateway.messages

import gateway.serial.SerialObject

data class GatewaySetColorCommand(
    val red: Int,
    val green: Int,
    val blue: Int
) : SerialObject {
    override val type: String =
        "set-color" // TODO: automatic way of defining the command / request / response tag
}

