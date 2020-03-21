package io.rtpi.service.irishrail

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.LiveTime
import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.Operator
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

    fun getLiveData(stationId: String): Single<List<IrishRailLiveData>> {
        return irishRailApi
            .getStationDataByCodeXml(stationCode = stationId)
            .map { validateResponse(it) }
    }

    private fun validateResponse(response: IrishRailStationDataResponseXml): List<IrishRailLiveData> =
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
                    IrishRailLiveData(
                        liveTime = createDueTime(xml),
                        operator = operator,
                        direction = xml.direction.validate(),
                        route = operator.fullName,
                        destination = xml.destination.validate(),
                        origin = xml.origin.validate()
                    )
                }
                .filter { !it.liveTime.waitTime.isNegative }
                .sortedBy { it.liveTime.waitTime }
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

    private fun createDueTime(xml: IrishRailStationDataXml): LiveTime {
        val serverDateTime = LocalDateTime.parse(xml.serverTime.validate(), DateTimeFormatter.ISO_DATE_TIME).atZone(dublin)
        val serverDate = serverDateTime.toLocalDate()

        val midnight = LocalTime.of(0, 0)
        val scheduledArrivalDateTime = parseTime(xml.schArrival.validate(), serverDate, serverDateTime)
        val expectedArrivalDateTime = parseTime(xml.expArrival.validate(), serverDate, serverDateTime)
        val scheduledDepartureDateTime = parseTime(xml.schDepart.validate(), serverDate, serverDateTime)
        val expectedDepartureDateTime = parseTime(xml.expDepart.validate(), serverDate, serverDateTime)

        val isStarting = scheduledArrivalDateTime.toLocalTime() == midnight && expectedArrivalDateTime.toLocalTime() == midnight
        val isTerminating = scheduledDepartureDateTime.toLocalTime() == midnight && expectedDepartureDateTime.toLocalTime() == midnight

        return LiveTime(
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
