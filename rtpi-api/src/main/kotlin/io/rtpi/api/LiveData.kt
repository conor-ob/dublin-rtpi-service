package io.rtpi.api

interface LiveData {

    val operator: Operator

}

interface TimedLiveData : LiveData {

    val route: String

    val destination: String

    val liveTimes: List<LiveTime>

}

data class LiveTime(
    val waitTimeSeconds: Int,
    val expectedTimestamp: String
//    val lateTimeSeconds: Int = 0,
//    val scheduledTimestamp: String = ""
)

data class AircoachLiveData(
    override val route: String,
    override val destination: String,
    override val liveTimes: List<LiveTime>
) : TimedLiveData {

    override val operator = Operator.AIRCOACH

}

data class BusEireannLiveData(
    override val route: String,
    override val destination: String,
    override val liveTimes: List<LiveTime>,
    override val operator: Operator
) : TimedLiveData

data class DublinBikesLiveData(
    val bikes: Int,
    val docks: Int
) : LiveData {

    override val operator = Operator.DUBLIN_BIKES

}

data class DublinBusLiveData(
    override val route: String,
    override val destination: String,
    override val liveTimes: List<LiveTime>,
    override val operator: Operator
) : TimedLiveData

data class IrishRailLiveData(
    override val route: String,
    override val destination: String,
    override val liveTimes: List<LiveTime>,
    override val operator: Operator,
    val direction: String,
    val origin: String
) : TimedLiveData

data class LuasLiveData(
    override val route: String,
    override val destination: String,
    override val liveTimes: List<LiveTime>,
    override val operator: Operator,
    val direction: String
) : TimedLiveData
