package io.rtpi.api

import java.time.Duration
import java.time.LocalTime
import java.time.ZonedDateTime

fun createDueTime(
    waitTimeMinutes: Duration,
    currentTime: LocalTime = LocalTime.now()
): Prediction {
    return Prediction(
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
    liveTime: Prediction
) = PredictionLiveData(
    service = Service.DUBLIN_BUS,
    operator = operator,
    route = Route(
        id = route,
        origin = origin,
        destination = destination,
        direction = direction
    ),
    prediction = liveTime
)

fun createIrishRailLiveData(
    currentTime: LocalTime = LocalTime.now(),
    waitTimeMinutes: Duration = Duration.ofMinutes(3),
    operator: Operator = Operator.DART,
    direction: String = "Southbound",
    destination: String = "Bray",
    route: String = operator.fullName,
    origin: String = "Howth"
) = PredictionLiveData(
    prediction = createDueTime(waitTimeMinutes, currentTime),
    operator = operator,
    service = Service.IRISH_RAIL,
    route = Route(
        id = route,
        direction = direction,
        destination = destination,
        origin = origin
    )
)

fun createLuasLiveData(
    currentTime: LocalTime = LocalTime.now(),
    waitTimeMinutes: Duration = Duration.ofMinutes(5),
    route: String = "Green Line",
    destination: String = "Sandyford",
    direction: String = "Outbound",
    origin: String = "St Stephen's Green"
) = PredictionLiveData(
    prediction = createDueTime(waitTimeMinutes, currentTime),
    operator = Operator.LUAS,
    service = Service.LUAS,
    route = Route(
        id = route,
        destination = destination,
        direction = direction,
        origin = origin
    )
)
