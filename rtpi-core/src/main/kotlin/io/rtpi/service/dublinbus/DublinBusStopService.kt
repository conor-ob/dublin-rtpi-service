package io.rtpi.service.dublinbus

import io.rtpi.api.Coordinate
import io.rtpi.api.DublinBusStop
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.dublinbus.DublinBusApi
import io.rtpi.resource.dublinbus.DublinBusDestinationRequestBodyXml
import io.rtpi.resource.dublinbus.DublinBusDestinationRequestRootXml
import io.rtpi.resource.dublinbus.DublinBusDestinationRequestXml
import io.rtpi.resource.dublinbus.DublinBusDestinationXml
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiBusStopInformationJson

class DublinBusStopService(
    private val dublinBusApi: DublinBusApi,
    private val rtpiApi: RtpiApi
) {

    fun getStops(): List<DublinBusStop> {
        val requestRoot = DublinBusDestinationRequestRootXml()
        val requestBody = DublinBusDestinationRequestBodyXml(requestRoot)
        val request = DublinBusDestinationRequestXml(requestBody)

        val dublinBusResponse = dublinBusApi.getAllDestinations(request)
            .validate()
            .stops
            .filter { xml ->
                xml.id != null
                    && xml.name != null
                    && xml.latitude != null
                    && xml.longitude != null
            }
        val rtpiDublinBusResponse = rtpiApi.busStopInformation(operator = Operator.DUBLIN_BUS.shortName, format = "json")
            .validate()
            .results
            .filter { json ->
                json.stopId != null
                    && json.fullName != null
                    && json.latitude != null
                    && json.longitude != null
            }
        val rtpiGoAheadResponse = rtpiApi.busStopInformation(operator = Operator.GO_AHEAD.shortName, format = "json")
            .validate()
            .results
            .filter { json ->
                json.stopId != null
                    && json.fullName != null
                    && json.latitude != null
                    && json.longitude != null
            }

        return aggregate(
            dublinBusResponse,
            rtpiDublinBusResponse,
            rtpiGoAheadResponse
        ).map { json ->
            DublinBusStop(
                id = json.stopId!!.trim(),
                name = json.fullName!!.trim(),
                coordinate = Coordinate(json.latitude!!.toDouble(), json.longitude!!.toDouble()),
                operators = json.operators.map { operator -> Operator.parse(operator.name!!.trim()) }.toSet(),
                routes = json.operators.associateBy( { Operator.parse(it.name!!.trim()) }, { it.routes } )
            )
        }
    }

    private fun aggregate(
        defaultStops: List<DublinBusDestinationXml>,
        dublinBusStops: List<RtpiBusStopInformationJson>,
        goAheadDublinStops: List<RtpiBusStopInformationJson>
    ): List<RtpiBusStopInformationJson> {
        val aggregatedStops = mutableMapOf<String, RtpiBusStopInformationJson>()
        for (stop in defaultStops) {
            var aggregatedStop = aggregatedStops[stop.id]
            if (aggregatedStop == null) {
                aggregatedStops[stop.id!!] = RtpiBusStopInformationJson(
                    stopId = stop.id!!,
                    fullName = stop.name!!,
                    latitude = stop.latitude!!,
                    longitude = stop.longitude!!
                )
            } else {
                aggregatedStop = aggregatedStop.copy(
                    stopId = stop.id!!,
                    fullName = stop.name!!,
                    latitude = stop.latitude!!,
                    longitude = stop.longitude!!
                )
                aggregatedStops[stop.id!!] = aggregatedStop
            }
        }
        for (stop in dublinBusStops) {
            var aggregatedStop = aggregatedStops[stop.stopId]
            if (aggregatedStop == null) {
                aggregatedStops[stop.stopId!!] = stop
            } else {
                val existingOperators = aggregatedStop.operators.toMutableList()
                existingOperators.addAll(stop.operators)
                aggregatedStop = aggregatedStop.copy(operators = existingOperators)
                aggregatedStops[stop.stopId!!] = aggregatedStop
            }
        }
        for (stop in goAheadDublinStops) {
            var aggregatedStop = aggregatedStops[stop.stopId]
            if (aggregatedStop == null) {
                aggregatedStops[stop.stopId!!] = stop
            } else {
                val existingOperators = aggregatedStop.operators.toMutableList()
                existingOperators.addAll(stop.operators)
                aggregatedStop = aggregatedStop.copy(operators = existingOperators)
                aggregatedStops[stop.stopId!!] = aggregatedStop
            }
        }
        return aggregatedStops.values.toList()
    }

}
