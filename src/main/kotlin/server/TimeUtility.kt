package server

interface TimeUtilityInterface {
    fun currentTimeMilliseconds(): Long
}

class TimeUtility: TimeUtilityInterface {

    override fun currentTimeMilliseconds(): Long {
        return System.currentTimeMillis()
    }

}