package io.rtpi.service.irishrail

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.Operator
import io.rtpi.api.Prediction
import io.rtpi.api.PredictionLiveData
import io.rtpi.api.RouteInfo
import io.rtpi.api.Service
import io.rtpi.external.irishrail.IrishRailApi
import io.rtpi.external.irishrail.IrishRailStationDataResponseXml
import io.rtpi.external.irishrail.IrishRailStationDataXml
import io.rtpi.validation.validate
import io.rtpi.validation.validateStrings
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val dublin = ZoneId.of("Europe/Dublin")

class IrishRailLiveDataService @Inject constructor(private val irishRailApi: IrishRailApi) {

    fun getLiveData(stationId: String): Single<List<LiveData>> {
        return irishRailApi
            .getStationDataByCodeXml(stationCode = stationId)
            .map { response -> validateResponse(response, stationId) }
    }

    private fun validateResponse(response: IrishRailStationDataResponseXml, stationId: String): List<LiveData> =
        if (response.stationData.isNullOrEmpty()) {
            emptyList()
        } else {
            requireNotNull(response.stationData)
                .filter { xml ->
                    validateStrings(
                        xml.trainType, xml.trainCode, xml.direction, xml.destination, xml.origin,
                        xml.serverTime, xml.dueIn, xml.schArrival, xml.schDepart, xml.expArrival, xml.schDepart
                    )
                }.map { xml ->
                    val operator = mapOperator(
                        xml.trainType.validate(),
                        xml.trainCode.validate()
                    )
                    PredictionLiveData(
                        prediction = createDueTime(xml),
                        operator = operator,
                        service = Service.IRISH_RAIL,
                        routeInfo = RouteInfo(
                            route = operator.fullName,
                            origin = xml.origin.validate(),
                            destination = xml.destination.validate(),
                            direction = xml.direction.validate()
                        )
                    )
                }
                .map { predictionLiveData ->
                    resolveKnownIssues(predictionLiveData, stationId)
                }
                .filter { !it.prediction.waitTime.isNegative }
                .sortedBy { it.prediction.waitTime }
        }

    /**
     * There are issues at Grand Canal Dock, Pearse, Tara Street and Connolly where the wrong Northbound/Southbound
     * information is given to Commuter trains probably because geographically the destination station may be north or
     * south of the current station but that is not the direction the train is travelling through the station
     */
    private fun resolveKnownIssues(liveData: PredictionLiveData, stationId: String): PredictionLiveData {
        if (liveData.operator == Operator.COMMUTER) {
            when (stationId) {
                "GCDK",
                "PERSE",
                "TARA",
                "CNLLY" -> {
                    val destination = liveData.routeInfo.destination
                    if ("Hazelhatch".equals(destination, ignoreCase = true) ||
                        "Newbridge".equals(destination, ignoreCase = true)
                    ) {
                        return liveData.copy(
                            routeInfo = liveData.routeInfo.copy(
                                direction = "Northbound"
                            )
                        )
                    } else if ("Grand Canal Dock".equals(destination, ignoreCase = true)) {
                        return liveData.copy(
                            routeInfo = liveData.routeInfo.copy(
                                direction = "Southbound"
                            )
                        )
                    }
                }
            }
        }
        return liveData
    }

    private fun mapOperator(trainType: String, trainCode: String): Operator {
        if (Operator.DART.shortName.equals(trainType, ignoreCase = true)) {
            return Operator.DART
        }
        return mapOperatorFromTrainCode(
            trainCode,
            trainType
        )
    }

    private fun mapOperatorFromTrainCode(trainCode: String, trainType: String): Operator {
        return when (trainCode.first().toString().toUpperCase()) {
            "E" -> Operator.DART
            "A", "B" -> Operator.INTERCITY
            "D", "P" -> Operator.COMMUTER
            else -> mapOperatorFromTrainType(
                trainType,
                trainCode
            )
        }
    }

    private fun mapOperatorFromTrainType(trainType: String, trainCode: String): Operator {
        return when (trainType.toUpperCase()) {
            "ARROW" -> Operator.COMMUTER
            "DART", "DART10" -> Operator.DART
            "DD/90", "ICR" -> Operator.INTERCITY
            else -> throw IllegalStateException("Unknown train type: $trainType, with code: $trainCode")
        }
    }

    private fun createDueTime(xml: IrishRailStationDataXml): Prediction {
        val serverDateTime = LocalDateTime.parse(xml.serverTime.validate(), DateTimeFormatter.ISO_DATE_TIME).atZone(dublin)
        val serverDate = serverDateTime.toLocalDate()

        val midnight = LocalTime.of(0, 0)
        val scheduledArrivalDateTime = parseTime(xml.schArrival.validate(), serverDate, serverDateTime)
        val expectedArrivalDateTime = parseTime(xml.expArrival.validate(), serverDate, serverDateTime)
        val scheduledDepartureDateTime = parseTime(xml.schDepart.validate(), serverDate, serverDateTime)
        val expectedDepartureDateTime = parseTime(xml.expDepart.validate(), serverDate, serverDateTime)

        val isStarting = scheduledArrivalDateTime.toLocalTime() == midnight && expectedArrivalDateTime.toLocalTime() == midnight
        val isTerminating = scheduledDepartureDateTime.toLocalTime() == midnight && expectedDepartureDateTime.toLocalTime() == midnight

        return Prediction(
            waitTime = Duration.ofMinutes(xml.dueIn.validate().toLong()),
            currentDateTime = serverDateTime,
            expectedDateTime = if (isTerminating) expectedArrivalDateTime else expectedDepartureDateTime,
            scheduledDateTime = if (isTerminating) scheduledArrivalDateTime else scheduledDepartureDateTime
        )
    }

    private fun parseTime(timestamp: String, date: LocalDate, serverDateTime: ZonedDateTime): ZonedDateTime {
        val dateTime = LocalTime.parse(timestamp).atDate(date).atZone(dublin)
        if (Duration.between(dateTime, serverDateTime).toHours() > 12) {
            return dateTime.plusDays(1)
        }
        return dateTime
    }
}
