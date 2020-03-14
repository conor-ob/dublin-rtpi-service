package io.rtpi.service.irishrail

import io.reactivex.Single
import io.rtpi.api.LiveTime
import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.Operator
import io.rtpi.external.irishrail.IrishRailApi
import io.rtpi.external.irishrail.IrishRailStationDataXml

abstract class AbstractIrishRailLiveDataService(private val irishRailApi: IrishRailApi) {

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

    protected abstract fun createDueTime(xml: IrishRailStationDataXml): LiveTime

}
