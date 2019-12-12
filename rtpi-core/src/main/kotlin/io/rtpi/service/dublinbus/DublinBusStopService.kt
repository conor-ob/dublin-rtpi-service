package io.rtpi.service.dublinbus

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import io.rtpi.api.Coordinate
import io.rtpi.api.DublinBusStop
import io.rtpi.api.Operator
import io.rtpi.api.Route
import io.rtpi.external.dublinbus.DublinBusApi
import io.rtpi.external.dublinbus.DublinBusDestinationRequestBodyXml
import io.rtpi.external.dublinbus.DublinBusDestinationRequestRootXml
import io.rtpi.external.dublinbus.DublinBusDestinationRequestXml
import io.rtpi.external.dublinbus.DublinBusDestinationXml
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson

class DublinBusStopService(
//    private val dublinBusApi: DublinBusApi,
    private val rtpiApi: RtpiApi
) {

    fun getStops(): Single<List<DublinBusStop>> {
        return Single.zip(
            getRtpiDublinBusStops(),
            getRtpiGoAheadStops(),
            BiFunction { t1, t2 ->
                t1.plus(t2)
                    .map { json ->
                        DublinBusStop(
                            id = json.stopId!!.trim(),
                            name = json.fullName!!.trim(),
                            coordinate = Coordinate(json.latitude!!.toDouble(), json.longitude!!.toDouble()),
                            operators = json.operators.map { operator -> Operator.parse(operator.name!!.trim()) }.toSet(),
                            routes = json.operators.flatMap { operator ->
                                operator.routes.map {
                                    Route(it.trim(), Operator.parse(operator.name!!))
                                }
                            }
                        )
                    }
            }
        )
//        return Single.zip(
//            getDublinBusStops(),
//            getRtpiDublinBusStops(),
//            getRtpiGoAheadStops(),
//            Function3 { t1, t2, t3 -> aggregate(t1, t2, t3).map { json ->
//                DublinBusStop(
//                    id = json.stopId!!.trim(),
//                    name = json.fullName!!.trim(),
//                    coordinate = Coordinate(json.latitude!!.toDouble(), json.longitude!!.toDouble()),
//                    operators = json.operators.map { operator -> Operator.parse(operator.name!!.trim()) }.toSet(),
//                    routes = json.operators.flatMap { operator ->
//                        operator.routes.map {
//                            Route(it.trim(), Operator.parse(operator.name!!))
//                        }
//                    }
//                )
//            }
//            }
//        )
    }

//    private fun getDublinBusStops(): Single<List<DublinBusDestinationXml>> {
////        val requestRoot = DublinBusDestinationRequestRootXml()
////        val requestBody = DublinBusDestinationRequestBodyXml(requestRoot)
////        val request = DublinBusDestinationRequestXml(requestBody)
////        return dublinBusApi.getAllDestinations(request)
////            .map { it.stops.filter { xml ->
////                xml.id != null
////                    && xml.name != null
////                    && xml.latitude != null
////                    && xml.longitude != null
////            }
////            }
//    }

    private fun getRtpiDublinBusStops(): Single<List<RtpiBusStopInformationJson>> {
        return rtpiApi.busStopInformation(operator = Operator.DUBLIN_BUS.shortName, format = "json")
            .map { it.results.filter { json ->
                json.stopId != null
                    && json.fullName != null
                    && json.latitude != null
                    && json.longitude != null
            }}
    }

    private fun getRtpiGoAheadStops(): Single<List<RtpiBusStopInformationJson>> {
        return rtpiApi.busStopInformation(operator = Operator.GO_AHEAD.shortName, format = "json")
            .map { it.results.filter { json ->
                json.stopId != null
                    && json.fullName != null
                    && json.latitude != null
                    && json.longitude != null
            } }
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
