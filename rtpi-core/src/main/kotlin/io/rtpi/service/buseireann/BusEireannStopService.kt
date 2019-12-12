package io.rtpi.service.buseireann

import io.reactivex.Single
import io.rtpi.api.BusEireannStop
import io.rtpi.api.Coordinate
import io.rtpi.api.Operator
import io.rtpi.api.Route
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.util.RouteComparator

class BusEireannStopService(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<BusEireannStop>> {
        return rtpiApi.busStopInformation(operator = "be", format = "json")
            .map { response ->
                response.results!!
                    .filter { json ->
                        json.stopId != null
                            && json.fullName != null
                            && json.latitude != null
                            && json.longitude != null
                            && json.operators != null
                    }.map { json ->
                        BusEireannStop(
                            id = json.stopId!!.trim(),
                            name = json.fullName!!.trim(),
                            coordinate = Coordinate(
                                latitude = json.latitude!!.toDouble(),
                                longitude = json.longitude!!.toDouble()
                            ),
                            operators = json.operators!!.map { operator ->
                                Operator.parse(operator.name!!.trim())
                            }.toSet(),
                            routes = json.operators!!.flatMap { operator ->
                                operator.routes!!.map { routeId ->
                                    Route(
                                        id = routeId.trim(),
                                        operator = Operator.parse(operator.name!!.trim())
                                    )
                                }
                            }.toSet().sortedWith(RouteComparator)
                        )
                    }
            }
    }

}
