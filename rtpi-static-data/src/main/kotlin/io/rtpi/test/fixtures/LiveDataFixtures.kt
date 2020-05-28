package io.rtpi.test.fixtures

import io.rtpi.api.DockLiveData
import io.rtpi.api.Operator
import io.rtpi.api.Prediction
import io.rtpi.api.PredictionLiveData
import io.rtpi.api.RouteInfo
import io.rtpi.api.Service
import java.time.Duration
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.random.Random

fun createPredictionLiveData(
    service: Service = Service.values().random(),
    operator: Operator = service.operators.random(),
    routeInfo: RouteInfo = createRouteInfo(),
    prediction: Prediction = createPrediction()
) = PredictionLiveData(service, operator, routeInfo, prediction)

fun createDockLiveData(
    service: Service = Service.values().random(),
    operator: Operator = service.operators.random(),
    availableBikes: Int = Random.nextInt(0, 50),
    availableDocks: Int = Random.nextInt(0, 50),
    totalDocks: Int = Random.nextInt(0, 50)
) = DockLiveData(service, operator, availableBikes, availableDocks, totalDocks)

fun createPrediction(
    waitTime: Duration = Duration.ofMinutes(Random.nextInt(0, 90).toLong()),
    currentDateTime: ZonedDateTime = ZonedDateTime.now(),
    scheduledDateTime: ZonedDateTime = ZonedDateTime.now().plus(waitTime),
    expectedDateTime: ZonedDateTime = ZonedDateTime.now().plus(waitTime)
) = Prediction(waitTime, currentDateTime, scheduledDateTime, expectedDateTime)

fun createRouteInfo(
    route: String = UUID.randomUUID().toString(),
    origin: String = UUID.randomUUID().toString(),
    destination: String = UUID.randomUUID().toString(),
    direction: String = UUID.randomUUID().toString()
) = RouteInfo(route, origin, destination, direction)
