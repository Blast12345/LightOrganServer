package utilities

import logging.Logger

class TimestampUtility(val name: String) {

    var lastTimestamp = System.nanoTime()

    fun reset() {
        lastTimestamp = System.nanoTime()
    }

    fun logTimeSinceLast() {
        val timeSinceLast = System.nanoTime() - lastTimestamp
        Logger.debug("$name: $timeSinceLast ns")

        lastTimestamp = System.nanoTime()
    }

}