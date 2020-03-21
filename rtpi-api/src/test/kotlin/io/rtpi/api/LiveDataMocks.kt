package io.rtpi.api

import java.time.LocalTime

fun createDueTime(
    waitTimeMinutes: Int,
    currentTime: LocalTime = LocalTime.now()
): LiveTime {
    return LiveTime(
        waitTime = waitTimeMinutes,
        currentDateTime = "",
        scheduledDateTime = "",
        expectedDateTime = ""
    )
}

fun createIrishRailLiveData(
    currentTime: LocalTime = LocalTime.now(),
    waitTimeMinutes: Int = 3,
    operator: Operator = Operator.DART,
    direction: String = "Southbound",
    destination: String = "Bray",
    route: String = operator.fullName,
    origin: String = "Howth"
): IrishRailLiveData {
    return IrishRailLiveData(
        liveTime = createDueTime(waitTimeMinutes, currentTime),
        operator = operator,
        direction = direction,
        destination = destination,
        route = route,
        origin = origin
    )
}

fun createLuasLiveData(
    currentTime: LocalTime = LocalTime.now(),
    waitTimeMinutes: Int = 5,
    route: String = "Green Line",
    destination: String = "Sandyford",
    direction: String = "Outbound",
    origin: String = "St Stephen's Green"
): LuasLiveData {
    return LuasLiveData(
        liveTime = createDueTime(waitTimeMinutes, currentTime),
        operator = Operator.LUAS,
        route = route,
        destination = destination,
        direction = direction,
        origin = origin
    )
}
