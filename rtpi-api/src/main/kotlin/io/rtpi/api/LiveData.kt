package io.rtpi.api

interface LiveData {

    val operator: Operator

}

interface TimedLiveData<T> : LiveData {

    val route: String

    val destination: String

    val nextDueTime: DueTime<T>

    val laterDueTimes: List<DueTime<T>>

}

data class DueTime<T>(
    val minutes: Int,
    val time: T
)

data class AircoachLiveData<T>(
    override val route: String,
    override val destination: String,
    override val nextDueTime: DueTime<T>,
    override val laterDueTimes: List<DueTime<T>>
) : TimedLiveData<T> {

    override val operator = Operator.AIRCOACH

}

data class BusEireannLiveData<T>(
    override val route: String,
    override val destination: String,
    override val nextDueTime: DueTime<T>,
    override val laterDueTimes: List<DueTime<T>>,
    override val operator: Operator
) : TimedLiveData<T>

data class DublinBikesLiveData(
    val bikes: Int,
    val docks: Int
) : LiveData {

    override val operator = Operator.DUBLIN_BIKES

}

data class DublinBusLiveData<T>(
    override val route: String,
    override val destination: String,
    override val nextDueTime: DueTime<T>,
    override val laterDueTimes: List<DueTime<T>>,
    override val operator: Operator
) : TimedLiveData<T>

data class IrishRailLiveData<T>(
    override val route: String,
    override val destination: String,
    override val nextDueTime: DueTime<T>,
    override val laterDueTimes: List<DueTime<T>>,
    override val operator: Operator,
    val direction: String
) : TimedLiveData<T>

data class LuasLiveData<T>(
    override val route: String,
    override val destination: String,
    override val nextDueTime: DueTime<T>,
    override val laterDueTimes: List<DueTime<T>>,
    override val operator: Operator,
    val direction: String
) : TimedLiveData<T>

data class SwordsExpressLiveData<T>(
    override val route: String,
    override val destination: String,
    override val nextDueTime: DueTime<T>,
    override val laterDueTimes: List<DueTime<T>>
) : TimedLiveData<T> {

    override val operator = Operator.SWORDS_EXPRESS

}
