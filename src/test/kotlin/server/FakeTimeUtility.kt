package server

class FakeTimeUtility: TimeUtilityInterface {

    var currentTimeInMillisecondsValue: Long = 123

    override fun currentTimeMilliseconds(): Long {
        return currentTimeInMillisecondsValue
    }

}