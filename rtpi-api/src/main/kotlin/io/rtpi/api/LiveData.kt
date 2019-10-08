package io.rtpi.api

interface LiveData {

    val operator: Operator

}

interface TimedLiveData : LiveData {

    val route: String

    val destination: String

    val times: List<Time>

}

data class Time(
    val minutes: Int
//    val hour: Int,
//    val minute: Int
)

data class AircoachLiveData(
    override val route: String,
    override val destination: String,
    override val times: List<Time>
) : TimedLiveData {

    override val operator = Operator.AIRCOACH

}

data class BusEireannLiveData(
    override val route: String,
    override val destination: String,
    override val times: List<Time>,
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
    override val times: List<Time>,
    override val operator: Operator
) : TimedLiveData

data class IrishRailLiveData(
    override val route: String,
    override val destination: String,
    override val times: List<Time>,
    override val operator: Operator,
    val direction: String
) : TimedLiveData

data class LuasLiveData(
    override val route: String,
    override val destination: String,
    override val times: List<Time>,
    override val operator: Operator,
    val direction: String
) : TimedLiveData

data class SwordsExpressLiveData(
    override val route: String,
    override val destination: String,
    override val times: List<Time>
) : TimedLiveData {

    override val operator = Operator.SWORDS_EXPRESS

}
