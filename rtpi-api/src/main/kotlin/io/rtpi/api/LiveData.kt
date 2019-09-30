package io.rtpi.api

interface LiveData {

    val operator: Operator

}

interface TimedLiveData<T> : LiveData {

    val nextDueTime: DueTime<T>

    val laterDueTimes: List<DueTime<T>>

    val route: String

    val destination: String

}

data class DueTime<T>(
    val minutes: Int,
    val time: T
)

data class AircoachLiveData<T>(
    override val nextDueTime: DueTime<T>,
    override val laterDueTimes: List<DueTime<T>>,
    override val route: String,
    override val destination: String
) : TimedLiveData<T> {

    override val operator = Operator.AIRCOACH

}

data class BusEireannLiveData<T>(
    override val nextDueTime: DueTime<T>,
    override val laterDueTimes: List<DueTime<T>>,
    override val operator: Operator,
    override val route: String,
    override val destination: String
) : TimedLiveData<T> {

//    override val operator = Operator.BUS_EIREANN

}

data class DublinBikesLiveData(
    val bikes: Int,
    val docks: Int
) : LiveData {

    override val operator = Operator.DUBLIN_BIKES

}

data class DublinBusLiveData<T>(
    override val nextDueTime: DueTime<T>,
    override val laterDueTimes: List<DueTime<T>>,
    override val operator: Operator,
    override val route: String,
    override val destination: String
) : TimedLiveData<T> {

}

data class IrishRailLiveData<T>(
    override val nextDueTime: DueTime<T>,
    override val laterDueTimes: List<DueTime<T>>,
    override val operator: Operator,
    override val route: String,
    override val destination: String,
    val direction: String
) : TimedLiveData<T> {

}

data class LuasLiveData<T>(
    override val nextDueTime: DueTime<T>,
    override val laterDueTimes: List<DueTime<T>>,
    override val operator: Operator,
    override val route: String,
    override val destination: String,
    val direction: String
) : TimedLiveData<T> {

//    override val operator = Operator.LUAS

}

data class SwordsExpressLiveData<T>(
    override val nextDueTime: DueTime<T>,
    override val laterDueTimes: List<DueTime<T>>,
    override val route: String,
    override val destination: String
) : TimedLiveData<T> {

    override val operator = Operator.SWORDS_EXPRESS

}
