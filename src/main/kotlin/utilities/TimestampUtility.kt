package utilities

import logging.Logger

class TimestampUtility(val name: String) {

    var lastTimestamp = System.currentTimeMillis()

    fun logTimeSinceLast() {
        val timeSinceLast = System.currentTimeMillis() - lastTimestamp
        Logger.debug("$name: $timeSinceLast")

        lastTimestamp = System.currentTimeMillis()
    }

}