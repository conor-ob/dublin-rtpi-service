package io.rtpi.api

import java.time.LocalTime

fun createDueTime(
    minutes: Int,
    currentTime: LocalTime = LocalTime.now()
): LiveTime {
    return LiveTime(
        waitTimeSeconds = minutes,
        expectedTimestamp = ""
//        time = currentTime.plusMinutes(minutes.toLong())
    )
}

fun createIrishRailLiveData(
    currentTime: LocalTime = LocalTime.now(),
    dueTimes: List<Int> = listOf(2, 14, 27),
    operator: Operator = Operator.DART,
    direction: String = "Southbound",
    destination: String = "Bray",
    route: String = operator.fullName,
    origin: String = "Howth"
): IrishRailLiveData {
    return IrishRailLiveData(
        liveTimes = dueTimes.map { createDueTime(it, currentTime) },
        operator = operator,
        direction = direction,
        destination = destination,
        route = route,
        origin = origin
    )
}

fun createLuasLiveData(
    laterDueTimes: List<Int> = listOf(5, 11, 14, 22),
    route: String = "Green Line",
    destination: String = "Sandyford",
    direction: String = "Outbound"
): LuasLiveData {
    return LuasLiveData(
        liveTimes = laterDueTimes.map { createDueTime(it) },
        operator = Operator.LUAS,
        route = route,
        destination = destination,
        direction = direction
    )
}