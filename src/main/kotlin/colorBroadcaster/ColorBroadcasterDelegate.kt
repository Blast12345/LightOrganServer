package colorBroadcaster

import java.awt.Color

interface ColorBroadcasterDelegate {
    fun getNextColor(): Color?
}
