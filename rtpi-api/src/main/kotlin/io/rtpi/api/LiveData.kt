package io.rtpi.api

import java.time.LocalTime

interface LiveData {

    val operator: Operator

}

interface TimedLiveData : LiveData {

    val nextDueTime: DueTime

    val laterDueTimes: List<DueTime>

    val route: String

    val destination: String

}

data class DueTime(
    val minutes: Int,
    val time: LocalTime
)

data class AircoachLiveData(
    override val nextDueTime: DueTime,
    override val laterDueTimes: List<DueTime>,
    override val route: String,
    override val destination: String
) : TimedLiveData {

    override val operator = Operator.AIRCOACH

}

data class BusEireannLiveData(
    override val nextDueTime: DueTime,
    override val laterDueTimes: List<DueTime>,
    override val operator: Operator,
    override val route: String,
    override val destination: String
) : TimedLiveData {

//    override val operator = Operator.BUS_EIREANN

}

data class DublinBikesLiveData(
    val bikes: Int,
    val docks: Int
) : LiveData {

    override val operator = Operator.DUBLIN_BIKES

}

data class DublinBusLiveData(
    override val nextDueTime: DueTime,
    override val laterDueTimes: List<DueTime>,
    override val operator: Operator,
    override val route: String,
    override val destination: String
) : TimedLiveData {

}

data class IrishRailLiveData(
    override val nextDueTime: DueTime,
    override val laterDueTimes: List<DueTime>,
    override val operator: Operator,
    override val route: String,
    override val destination: String,
    val direction: String
) : TimedLiveData {
    
}

data class LuasLiveData(
    override val nextDueTime: DueTime,
    override val laterDueTimes: List<DueTime>,
    override val operator: Operator,
    override val route: String,
    override val destination: String,
    val direction: String
) : TimedLiveData {

//    override val operator = Operator.LUAS
    
}

data class SwordsExpressLiveData(
    override val nextDueTime: DueTime,
    override val laterDueTimes: List<DueTime>,
    override val route: String,
    override val destination: String
) : TimedLiveData {

    override val operator = Operator.SWORDS_EXPRESS

}
