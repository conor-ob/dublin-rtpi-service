package io.rtpi.api

import java.time.LocalTime

fun createDueTime(
    minutes: Int,
    currentTime: LocalTime = LocalTime.now()
): Time<LocalTime> {
    return Time(
        minutes = minutes,
        time = currentTime.plusMinutes(minutes.toLong())
    )
}

fun createIrishRailLiveData(
    nextDueTime: Int = 2,
    currentTime: LocalTime = LocalTime.now(),
    laterDueTimes: List<Int> = listOf(14, 27),
    operator: Operator = Operator.DART,
    direction: String = "Southbound",
    destination: String = "Bray",
    route: String = operator.fullName
): IrishRailLiveData<LocalTime> {
    return IrishRailLiveData(
        nextDueTime = createDueTime(nextDueTime, currentTime),
        laterDueTimes = laterDueTimes.map { createDueTime(it, currentTime) },
        operator = operator,
        direction = direction,
        destination = destination,
        route = route
    )
}

fun createLuasLiveData(
    nextDueTime: Int = 5,
    laterDueTimes: List<Int> = listOf(11, 14, 22),
    route: String = "Green Line",
    destination: String = "Sandyford",
    direction: String = "Outbound"
): LuasLiveData<LocalTime> {
    return LuasLiveData(
        nextDueTime = createDueTime(nextDueTime),
        laterDueTimes = laterDueTimes.map { createDueTime(it) },
        operator = Operator.LUAS,
        route = route,
        destination = destination,
        direction = direction
    )
}