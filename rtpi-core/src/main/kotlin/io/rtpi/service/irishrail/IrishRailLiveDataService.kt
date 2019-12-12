package io.rtpi.service.irishrail

import io.reactivex.Single
import io.rtpi.api.LiveTime
import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.Operator
import io.rtpi.external.irishrail.IrishRailApi
import io.rtpi.external.irishrail.IrishRailStationDataXml
import io.rtpi.time.toIso8601
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val dublin = ZoneId.of("Europe/Dublin")

class IrishRailLiveDataService(private val irishRailApi: IrishRailApi) {

    fun getLiveData(stationId: String): Single<List<IrishRailLiveData>> {
        return irishRailApi.getStationDataByCodeXml(
            stationCode = stationId
        ).map { response ->
            response.stationData!!
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
                .filter { it.liveTime.waitTimeMinutes >= 0 }
                .sortedBy { it.liveTime.waitTimeMinutes }
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
            waitTimeMinutes = xml.dueIn!!.toInt(),
            currentTimestamp = serverDateTime.toIso8601(),
            expectedTimestamp = if (isTerminating) expectedArrivalDateTime.toIso8601() else expectedDepartureDateTime.toIso8601(),
            scheduledTimestamp = if (isTerminating) scheduledArrivalDateTime.toIso8601() else scheduledDepartureDateTime.toIso8601()
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
