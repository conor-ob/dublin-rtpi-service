package io.rtpi.service.irishrail

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.LiveTime
import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.Operator
import io.rtpi.external.irishrail.IrishRailApi
import io.rtpi.external.irishrail.IrishRailStationDataXml
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
        return irishRailApi.getStationDataByCodeXml(
            stationCode = stationId
        ).map { response ->
            response.stationData!! //TODO this could be null
                .filter { xml ->
                    xml.trainType != null
                        && xml.trainCode != null
                        && xml.direction != null
                        && xml.destination != null
                        && xml.origin != null
                        && xml.serverTime != null
                        && xml.dueIn != null
                        && xml.schArrival != null
                        && xml.schDepart != null
                        && xml.expArrival != null
                        && xml.expDepart != null
                }.map { xml ->
                    val operator = mapOperator(xml.trainType!!.trim(), xml.trainCode!!.trim())
                    IrishRailLiveData(
                        liveTime = createDueTime(xml),
                        operator = operator,
                        direction = xml.direction!!.trim(),
                        route = operator.fullName,
                        destination = xml.destination!!.trim(),
                        origin = xml.origin!!.trim()
                    )
                }
                .filter { it.liveTime.waitTime.isPositive() }
                .sortedBy { it.liveTime.waitTime }
        }
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
        val serverDateTime = LocalDateTime.parse(xml.serverTime, DateTimeFormatter.ISO_DATE_TIME).atZone(dublin)
        val serverDate = serverDateTime.toLocalDate()

        val midnight = LocalTime.of(0, 0)
        val scheduledArrivalDateTime = parseTime(xml.schArrival!!, serverDate, serverDateTime)
        val expectedArrivalDateTime = parseTime(xml.expArrival!!, serverDate, serverDateTime)
        val scheduledDepartureDateTime = parseTime(xml.schDepart!!, serverDate, serverDateTime)
        val expectedDepartureDateTime = parseTime(xml.expDepart!!, serverDate, serverDateTime)

        val isStarting = scheduledArrivalDateTime.toLocalTime() == midnight && expectedArrivalDateTime.toLocalTime() == midnight
        val isTerminating = scheduledDepartureDateTime.toLocalTime() == midnight && expectedDepartureDateTime.toLocalTime() == midnight

        return LiveTime(
            waitTime = Duration.ofMinutes(xml.dueIn!!.toLong()),
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

fun Duration.isPositive(): Boolean = !isNegative && !isZero
