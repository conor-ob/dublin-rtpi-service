package io.rtpi.api

import java.time.Duration
import java.time.ZonedDateTime

interface LiveData {
    val service: Service
    val operator: Operator
}

data class PredictionLiveData(
    override val service: Service,
    override val operator: Operator,
    val routeInfo: RouteInfo,
    val prediction: Prediction
) : LiveData

data class DockLiveData(
    override val service: Service,
    override val operator: Operator,
    val availableBikes: Int,
    val availableDocks: Int,
    val totalDocks: Int
) : LiveData

data class RouteInfo(
    val route: String,
    val origin: String,
    val destination: String,
    val direction: String
)

data class Prediction(
    val waitTime: Duration,
    val currentDateTime: ZonedDateTime,
    val scheduledDateTime: ZonedDateTime,
    val expectedDateTime: ZonedDateTime
)
