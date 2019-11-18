package io.rtpi.api

interface LiveData {

    val operator: Operator

}

interface TimedLiveData : LiveData {

    val route: String

    val origin: String

    val destination: String

    val direction: String

    val liveTime: LiveTime

}

data class LiveTime(
    val waitTimeMinutes: Int = 0,
    val currentTimestamp: String = "",
    val scheduledArrivalTimestamp: String? = null,
    val expectedArrivalTimestamp: String? = null,
    val scheduledDepartureTimestamp: String? = null,
    val expectedDepartureTimestamp: String? = null
)

data class AircoachLiveData(
    override val route: String,
    override val origin: String,
    override val destination: String,
    override val direction: String,
    override val liveTime: LiveTime
) : TimedLiveData {

    override val operator = Operator.AIRCOACH

}

data class BusEireannLiveData(
    override val operator: Operator,
    override val route: String,
    override val origin: String,
    override val destination: String,
    override val direction: String,
    override val liveTime: LiveTime
) : TimedLiveData

data class DublinBikesLiveData(
    val bikes: Int,
    val docks: Int
) : LiveData {

    override val operator = Operator.DUBLIN_BIKES

}

data class DublinBusLiveData(
    override val operator: Operator,
    override val route: String,
    override val origin: String,
    override val destination: String,
    override val direction: String,
    override val liveTime: LiveTime
) : TimedLiveData

data class IrishRailLiveData(
    override val operator: Operator,
    override val route: String,
    override val origin: String,
    override val destination: String,
    override val direction: String,
    override val liveTime: LiveTime
) : TimedLiveData

data class LuasLiveData(
    override val operator: Operator,
    override val route: String,
    override val origin: String,
    override val destination: String,
    override val direction: String,
    override val liveTime: LiveTime
) : TimedLiveData
