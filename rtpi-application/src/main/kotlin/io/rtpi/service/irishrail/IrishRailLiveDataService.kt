package io.rtpi.service.irishrail

import io.rtpi.api.DueTime
import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.irishrail.IrishRailApi
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class IrishRailLiveDataService(private val irishRailApi: IrishRailApi) {

    fun getLiveData(stationId: String, compact: Boolean): List<IrishRailLiveData> {
        return irishRailApi.getStationDataByCodeXml(stationId)
            .validate()
            .stationData
            .map { xml ->
                val operator = mapOperator(xml.trainType!!, xml.trainCode!!)
                IrishRailLiveData(
                    nextDueTime = mapDueTime(xml.expArrival!!, xml.dueIn!!, xml.queryTime!!),
                    laterDueTimes = emptyList(),
                    operator = operator,
                    direction = xml.direction!!,
                    route = operator.fullName,
                    destination = xml.destination!!
                )
            }
    }

    private fun mapDueTime(expectedArrivalTimestamp: String, dueInMinutes: String, queryTime: String): DueTime {
        if (expectedArrivalTimestamp == "00:00") {
            val now = LocalTime.parse(queryTime, DateTimeFormatter.ofPattern("HH:mm:ss"))
            val expectedTime = now.plusMinutes(dueInMinutes.toLong())
            return DueTime(dueInMinutes.toInt(), expectedTime)
        }
        val expectedTime = LocalTime.parse(expectedArrivalTimestamp, DateTimeFormatter.ofPattern("HH:mm"))
        return DueTime(dueInMinutes.toInt(), expectedTime)
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

}