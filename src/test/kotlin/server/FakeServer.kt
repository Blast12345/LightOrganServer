package server

import java.awt.Color

class FakeServer: ServerInterface {

    var color: Color? = null
    override var lastColorTimestampInMilliseconds: Long? = null
    override var millisecondsSinceLastSentColor: Long? = null

    override fun sendColor(color: Color) {
        this.color = color
    }

}