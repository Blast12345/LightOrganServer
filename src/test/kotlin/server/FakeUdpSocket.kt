package server

class FakeUdpSocket: UdpSocketInterface {

    var message: String? = null
    var address: String? = null
    var port: Int? = null

    override fun send(message: String, address: String, port: Int) {
        this.message = message
        this.address = address
        this.port = port
    }

}