package io.rtpi.api

import java.time.Duration
import java.time.ZonedDateTime

interface LiveData {
    val operator: Operator
    val service: Service
}

data class PredictionLiveData(
    override val operator: Operator,
    override val service: Service,
    val route: Route,
    val prediction: Prediction
) : LiveData

data class DockLiveData(
    override val operator: Operator,
    override val service: Service,
    val availableBikes: Int,
    val availableDocks: Int,
    val totalDocks: Int
) : LiveData

data class Route(
    val id: String,
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
