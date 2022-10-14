package server

class FakeServer: ServerInterface {

    var message: String? = null
    override var lastMessageTimestampInMilliseconds: Long? = null
    override var millisecondsSinceLastSentMessage: Long? = null

    override fun sendMessage(message: String) {
        this.message = message
    }

}