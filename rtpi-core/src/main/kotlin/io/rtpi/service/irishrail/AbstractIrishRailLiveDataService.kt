package io.rtpi.service.irishrail

import io.reactivex.Single
import io.rtpi.api.LiveTime
import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.Operator
import io.rtpi.resource.irishrail.IrishRailApi
import io.rtpi.resource.irishrail.IrishRailStationDataResponseXml
import io.rtpi.resource.irishrail.IrishRailStationDataXml
import java.util.Objects

abstract class AbstractIrishRailLiveDataService(private val irishRailApi: IrishRailApi) {

    fun getLiveData(stationId: String): Single<List<IrishRailLiveData>> {
        return irishRailApi.getStationDataByCodeXml(stationId)
            .map { mapResponse(it) }
    }

    private fun mapResponse(response: IrishRailStationDataResponseXml): List<IrishRailLiveData> {
        val liveData = response.stationData.map { xml ->
            val operator = mapOperator(xml.trainType!!, xml.trainCode!!)
            IrishRailLiveData(
                liveTimes = listOf(createDueTime(xml)),
                operator = operator,
                direction = xml.direction!!,
                route = operator.fullName,
                destination = xml.destination!!,
                origin = xml.origin!!
            )
        }.sortedBy { it.liveTimes.first().waitTimeSeconds }

        val condensedLiveData = LinkedHashMap<Int, IrishRailLiveData>()
        for (data in liveData) {
            val id = Objects.hash(data.operator, data.route, data.destination, data.direction)
            var cachedLiveData = condensedLiveData[id]
            if (cachedLiveData == null) {
                condensedLiveData[id] = data
            } else {
                val dueTimes = cachedLiveData.liveTimes.toMutableList()
                dueTimes.add(data.liveTimes.first())
                cachedLiveData = cachedLiveData.copy(liveTimes = dueTimes)
                condensedLiveData[id] = cachedLiveData
            }
        }
        return condensedLiveData.values.toList()
    }

//    override fun id(liveData: IrishRailLiveData): Int =
//        Objects.hash(liveData.operator, liveData.route, liveData.destination, liveData.direction)

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
