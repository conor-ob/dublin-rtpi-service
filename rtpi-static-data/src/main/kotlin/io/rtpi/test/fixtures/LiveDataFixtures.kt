package io.rtpi.test.fixtures

import io.rtpi.api.Operator
import io.rtpi.api.Prediction
import io.rtpi.api.PredictionLiveData
import io.rtpi.api.RouteInfo
import io.rtpi.api.Service
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
    routeInfo = RouteInfo(
        route = route,
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
    routeInfo = RouteInfo(
        route = route,
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
    routeInfo = RouteInfo(
        route = route,
        destination = destination,
        direction = direction,
        origin = origin
    )
)