package io.rtpi.api

import java.time.Duration
import java.time.LocalTime
import java.time.ZonedDateTime

fun createDueTime(
    waitTimeMinutes: Duration,
    currentTime: LocalTime = LocalTime.now()
): LiveTime {
    return LiveTime(
        waitTime = waitTimeMinutes,
        currentDateTime = ZonedDateTime.now(),
        scheduledDateTime = ZonedDateTime.now(),
        expectedDateTime = ZonedDateTime.now()
    )
}

fun createDublinBusLiveData(
    operator: Operator,
    route: String,
    origin: String,
    destination: String,
    direction: String,
    liveTime: LiveTime
) = DublinBusLiveData(
    operator = operator,
    route = route,
    origin = origin,
    destination = destination,
    direction = direction,
    liveTime = liveTime
)

fun createIrishRailLiveData(
    currentTime: LocalTime = LocalTime.now(),
    waitTimeMinutes: Duration = Duration.ofMinutes(3),
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
    waitTimeMinutes: Duration = Duration.ofMinutes(5),
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
