package io.rtpi.service.dublinbus

import com.google.inject.Inject
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.rtpi.api.Coordinate
import io.rtpi.api.DublinBusStop
import io.rtpi.api.Operator
import io.rtpi.api.Route
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiBusStopInformationJson
import io.rtpi.util.RouteComparator

class DublinBusStopService @Inject constructor(private val rtpiApi: RtpiApi) {

    fun getStops(): Single<List<DublinBusStop>> {
        return Single.zip(
            getStops(Operator.DUBLIN_BUS),
            getStops(Operator.GO_AHEAD),
            BiFunction { dublinBusStops, goAheadStops ->
                aggregate(dublinBusStops, goAheadStops)
            }
        )
    }

    private fun aggregate(
        dublinBusStops: List<RtpiBusStopInformationJson>,
        goAheadStops: List<RtpiBusStopInformationJson>
    ): List<DublinBusStop> {
        val aggregated = dublinBusStops.associateBy { it.stopId }.toMutableMap()
        for (goAheadStop in goAheadStops) {
            var existing = aggregated[goAheadStop.stopId]
            if (existing == null) {
                aggregated[goAheadStop.stopId] = goAheadStop
            } else {
                val existingOperators = existing.operators.toMutableList()
                existingOperators.addAll(goAheadStop.operators)
                existing = existing.copy(
                    operators = existingOperators
                )
                aggregated[goAheadStop.stopId] = existing
            }
        }
        return aggregated.values
            .map { json ->
                DublinBusStop(
                    id = json.stopId!!.trim(),
                    name = json.fullName!!.trim(),
                    coordinate = Coordinate(
                        latitude = json.latitude!!.toDouble(),
                        longitude = json.longitude!!.toDouble()
                    ),
                    operators = json.operators.map { operator ->
                        Operator.parse(operator.name!!.trim())
                    }.toSet(),
                    routes = json.operators.flatMap { operator ->
                        operator.routes.map { routeId ->
                            Route(
                                id = routeId.trim(),
                                operator = Operator.parse(operator.name!!.trim())
                            )
                        }
                    }.toSet().sortedWith(RouteComparator)
                )
            }
    }

    private fun getStops(operator: Operator): Single<List<RtpiBusStopInformationJson>> {
        return rtpiApi.busStopInformation(
            operator = operator.shortName,
            format = "json"
        ).map { response ->
            response.results
                .filter { json ->
                    json.stopId != null
                        && json.fullName != null
                        && json.latitude != null
                        && json.longitude != null
                        && json.operators.isNotEmpty()
                }
        }
    }

}
