package io.rtpi.service.irishrail

import io.rtpi.api.Time
import io.rtpi.api.IrishRailLiveData
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.irishrail.IrishRailApi
import java.util.Objects

abstract class AbstractIrishRailLiveDataService(private val irishRailApi: IrishRailApi) {

    fun getLiveData(stationId: String): List<IrishRailLiveData> {
        val liveData = irishRailApi.getStationDataByCodeXml(stationId)
            .validate()
            .stationData
            .map { xml ->
                val operator = mapOperator(xml.trainType!!, xml.trainCode!!)
                IrishRailLiveData(
                    times = listOf(createDueTime(xml.expArrival!!, xml.dueIn!!, xml.queryTime!!)),
                    operator = operator,
                    direction = xml.direction!!,
                    route = operator.fullName,
                    destination = xml.destination!!
                )
            }
            .sortedBy { it.times.first().minutes }

        val condensedLiveData = LinkedHashMap<Int, IrishRailLiveData>()
        for (data in liveData) {
            val id = Objects.hash(data.operator, data.route, data.destination, data.direction)
            var cachedLiveData = condensedLiveData[id]
            if (cachedLiveData == null) {
                condensedLiveData[id] = data
            } else {
                val dueTimes = cachedLiveData.times.toMutableList()
                dueTimes.add(data.times.first())
                cachedLiveData = cachedLiveData.copy(times = dueTimes)
                condensedLiveData[id] = cachedLiveData
            }
        }
//        return condensedLiveData.values.toList()

        return listOf(
            IrishRailLiveData(
                route = Operator.DART.fullName,
                destination = "Dummy Data",
                direction = "Northbound",
                operator = Operator.DART,
                times = listOf(
                    Time(0),
                    Time(7),
                    Time(42)
                )
            )
        )
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

    protected abstract fun createDueTime(expectedArrivalTimestamp: String, dueInMinutes: String, queryTime: String): Time

}
